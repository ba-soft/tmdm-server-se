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
package com.amalto.commons.core.utils.xpath.ri.compiler;

import java.util.ArrayList;


/**
 * Implementation of {@link Expression} for the operation "and".
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class CoreOperationAnd extends CoreOperation {

    /**
     * Create a new CoreOperationAnd.
     * @param args to combine
     */
    public CoreOperationAnd(Expression[] args) {
        super(args);
    }

//    public Object computeValue(EvalContext context) {
//        for (int i = 0; i < args.length; i++) {
//            if (!InfoSetUtil.booleanValue(args[i].computeValue(context))) {
//                return Boolean.FALSE;
//            }
//        }
//        return Boolean.TRUE;
//    }

    protected int getPrecedence() {
        return AND_PRECEDENCE;
    }

    protected boolean isSymmetric() {
        return true;
    }

    public String getSymbol() {
        return "and";
    }

    public CoreOperationAnd clone(boolean deep) {
    	Expression[] arguments;
    	if (! deep) {
    		arguments = this.args;
    	} else {
    		ArrayList<Expression> as = new ArrayList<Expression>();
        	if (this.args != null) {
        		for (int i = 0; i < this.args.length; i++) {
        			as.add(this.args[i].clone(true));
                }
        	}
        	arguments = as.toArray(new Expression[as.size()]);
    	}
    	return new CoreOperationAnd(arguments);
    }
}
