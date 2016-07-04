/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.seaborne.tdb2.graph;

import org.apache.jena.query.Dataset ;
import org.apache.jena.query.ReadWrite ;
import org.junit.After ;
import org.junit.Before ;
import org.seaborne.dboe.transaction.Txn ;
import org.seaborne.tdb2.TDB2Factory ;

public class TestGraphsTDB2 extends AbstractTestGraphsTDB
{
    // Transactional.
    @Before
    public void before() {
        getDataset().begin(ReadWrite.READ) ;
    }

    @After
    public void after() {
        getDataset().end() ;
    }

    @Override
    protected void fillDataset(Dataset dataset) {
        Txn.execWrite(dataset, ()->{
            super.fillDataset(dataset) ;
        });
    }
    
    @Override
    protected Dataset createDataset() {
        return TDB2Factory.createDataset() ;
    }
}
