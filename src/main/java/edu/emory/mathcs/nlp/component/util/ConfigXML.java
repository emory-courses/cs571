/**
 * Copyright 2014, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.component.util;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public interface ConfigXML
{
	String LANGUAGE		= "language";
	String MODEL		= "model";
	
	String FIELD_ID		= "id";
	String FIELD_FORM	= "form";
	String FIELD_LEMMA	= "lemma";
	String FIELD_POS 	= "pos";
	String FIELD_FEATS 	= "feats";
	String FIELD_HEADID	= "headId";
	String FIELD_DEPREL	= "deprel";
	String FIELD_SHEADS	= "sheads";
	String FIELD_NAMENT	= "nament";
	String FIELD_COREF	= "coref";
	String FIELD_XHEADS	= "xheads";
	String FIELD_SEQTAG	= "seqtag";
	
//	========================== TSV READER ==========================

	String TSV		= "tsv";
	String COLUMN	= "column";
	String FIELD	= "field";
	String INDEX	= "index";
	
//	========================== TRAINER ==========================

	String TRAINER			= "trainer";
	String BOOTSTRAP		= "bootstrap";
	
	String ALGORITHM		= "algorithm";
	String LABEL_CUTOFF		= "label_cutoff";
	String FEATURE_CUTOFF	= "feature_cutoff";
	String LEARNING_RATE	= "learning_rate";
	String AVERAGE			= "average";
	String BIAS				= "bias";
	String RIDGE			= "ridge";
	String BINOMIAL			= "binomial";
	String RESET			= "reset";
	
//	========================== ALGORITHMS ==========================
	
	String PERCEPTRON		= "perceptron";
	String ADAGRAD_HINGE	= "adagrad-hinge";
}
