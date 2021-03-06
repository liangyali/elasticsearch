/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.indexedscripts.delete;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.DelegatingActionListener;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * Performs the delete operation.
 */
public class TransportDeleteIndexedScriptAction extends HandledTransportAction<DeleteIndexedScriptRequest,  DeleteIndexedScriptResponse> {

    private ScriptService scriptService;
    private Client client;

    @Inject
    public TransportDeleteIndexedScriptAction(Settings settings, ThreadPool threadPool, ScriptService scriptService,
                                              Client client, TransportService transportService, ActionFilters actionFilters) {
        super(settings, DeleteIndexedScriptAction.NAME, threadPool, transportService, actionFilters);
        this.scriptService = scriptService;
        this.client = client;
    }

    @Override
    public DeleteIndexedScriptRequest newRequestInstance(){
        return new DeleteIndexedScriptRequest();
    }

    @Override
    protected void doExecute(final DeleteIndexedScriptRequest request, final ActionListener<DeleteIndexedScriptResponse> listener) {
        scriptService.deleteScriptFromIndex(client, request.scriptLang(), request.id(), request.version(), new DelegatingActionListener<DeleteResponse, DeleteIndexedScriptResponse>(listener) {
            @Override
            public DeleteIndexedScriptResponse getDelegatedFromInstigator(DeleteResponse deleteResponse){
                return new DeleteIndexedScriptResponse(deleteResponse.getIndex(),deleteResponse.getType(),deleteResponse.getType(),deleteResponse.getVersion(), deleteResponse.isFound());
            }
        });
    }
}
