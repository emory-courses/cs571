/**
 * Copyright 2015, Emory University
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
package edu.emory.mathcs.nlp.component.pos;

import java.io.Serializable;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public interface IPOSNode extends Serializable
{
	/** @return the word-form of this node. */
	String getWordForm();
	/** @return the previous word-form of this node. */
	String setWordForm(String form);
	/** @return the part-of-speech tag of this node. */
	String getPOSTag();
	/** @return the previous part-of-speech tag of this node. */
	String setPOSTag(String tag);
}
