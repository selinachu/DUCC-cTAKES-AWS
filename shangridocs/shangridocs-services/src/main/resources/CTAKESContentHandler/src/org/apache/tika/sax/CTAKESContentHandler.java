/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.sax;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.serialization.JsonMetadataList;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import org.apache.commons.io.FileUtils;

/**
 * Class used to extract biomedical information while parsing. 
 *
 * <p>
 * This class relies on <a href="http://ctakes.apache.org/">Apache cTAKES</a> 
 * that is a natural language processing system for extraction of information 
 * from electronic medical record clinical free-text.
 * </p>
 *
 */
public class CTAKESContentHandler {

	// Prefix used for metadata including cTAKES annotations
	public static String CTAKES_META_PREFIX = "ctakes:";

	// Configuration object for CTAKESContentHandler
	private static CTAKESConfig config = null;

	// Metadata object used for cTAKES annotations
	private static Metadata metadata = null;
	
	/**
	 * Constructor for CTAKESContentHandler.
	 * @param metadata {@see Metadata} object used to store information 
	 * extracted by cTAKES.
	 * @param config {@see CTAKESConfig} object used to configure cTAKES 
	 * analysis engine.
	 */
	public CTAKESContentHandler(Metadata metadata, CTAKESConfig config) {
		this.metadata = metadata;
		this.config = config;
	}
	
	/**
	 * Returns metadata that includes cTAKES annotations.
	 * @return {@Metadata} object that includes cTAKES annotations.
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * Deserializes XML-based output from cTAKES to jCas object.
	 * @param stream of XML file.
	 * @param jcas {@see jCas} object used to keep the jcas from XML file.
	 */
	public static void deserialize(InputStream stream, JCas jcas) {
		try {
			XCASDeserializer.deserialize(new BufferedInputStream(stream), jcas.getCas());
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		} catch (SAXException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				ioe.printStackTrace();
			}
		}
	}
	
	/**
	 * Serializes metadata to output file.
	 * @param outputFile to output metadata.
	 * @param inputFileName of input file 
	 * @param jcas {@see jCas} object used to keep the jcas from XML file.
	 */
	public static void serializeAsMetadata(File outputFile, String inputFileName, JCas jcas) throws SAXException {
		try {
			// Add annotations to metadata
	        metadata.add(CTAKES_META_PREFIX + "schema", config.getAnnotationPropsAsString());
	        CTAKESAnnotationProperty[] annotationPros = config.getAnnotationProps();
	        Collection<IdentifiedAnnotation> collection = JCasUtil.select(jcas, IdentifiedAnnotation.class);
	        Iterator<IdentifiedAnnotation> iterator = collection.iterator();
	        while (iterator.hasNext()) {
	            IdentifiedAnnotation annotation = iterator.next();
	            StringBuilder annotationBuilder = new StringBuilder();
	            annotationBuilder.append(annotation.getCoveredText());
	            if (annotationPros != null) {
	                for (CTAKESAnnotationProperty property : annotationPros) {
	                    annotationBuilder.append(config.getSeparatorChar());
	                    annotationBuilder.append(CTAKESUtils.getAnnotationProperty(annotation, property));
	                }
	            }
	            metadata.add(CTAKES_META_PREFIX + annotation.getType().getShortName(), annotationBuilder.toString());
	        }
	        // For the missing file content and title
                System.out.println("Metadata:"+metadata+"JCas:"+jcas.getSofa());
	        metadata.add("X-TIKA:content", jcas.getSofa().getLocalStringData());
	        metadata.add("title", inputFileName.substring(inputFileName.lastIndexOf('/') + 1));

	        List<Metadata> metadataList = new LinkedList<Metadata>();
	        metadataList.add(metadata);
	        StringWriter writer = new StringWriter();
        	JsonMetadataList.toJson(metadataList, writer);

			FileUtils.writeStringToFile(outputFile, writer.toString());
	    } catch (Exception e) {
                e.printStackTrace();
            throw new SAXException(e.getMessage());
        } finally {
            CTAKESUtils.resetCAS(jcas);
        }
	}
}
