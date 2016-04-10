/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.tika.sax.CTAKESConfig;
import org.apache.tika.sax.CTAKESContentHandler;

/**
 * This class deserializes XML-based output from cTAKES and then writes out the 
 * identified annotations as Tika metadata object information.  
 *
 */
public class CTAKESContentToMetadataHandler {

	private static void usage() {
		System.err.println("Usage: " + CTAKESContentToMetadataHandler.class.getName() + " -i /path/to/input -o /path/to/output");
		System.exit(1);
	}

	public static void main(String[] args) throws Exception {		
		String input = null;
		String output = null;
		
		for (int i = 0; i < args.length; i++) {
			if ("-i".equals(args[i])) {
				input = args[++i];
			} else if ("-o".equals(args[i])) {
				output = args[++i];
			} else {
				usage();
			}
		}

		if ((input == null) || (output == null)) {
			usage();
		}

		File inputFile = new File(input);
		File outputFile = new File(output);
		
		if (!inputFile.isFile() || !inputFile.canRead()) {
			System.err.println("Error: " + input + " is not a file or cannot be read!");
			System.exit(1);
		}

		InputStream inputStream = new FileInputStream(inputFile);
		CTAKESConfig config = new CTAKESConfig();
		Metadata metadata = new Metadata();
		CTAKESContentHandler handler = new CTAKESContentHandler(metadata, config);
		final JCas jcas = JCasFactory.createJCas();

		handler.deserialize(inputStream, jcas);
		
		handler.serializeAsMetadata(outputFile, input, jcas);
	}
}