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

import edu.emory.mathcs.nlp.common.collection.node.POSNode;
import edu.emory.mathcs.nlp.component.state.L2RState;

/**
 * Part-of-speech tagging state.
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class POSState<N extends POSNode> extends L2RState<N>
{
	public POSState(N[] nodes)
	{
		super(nodes, N::getPOSTag, N::setPOSTag);
	}
}
