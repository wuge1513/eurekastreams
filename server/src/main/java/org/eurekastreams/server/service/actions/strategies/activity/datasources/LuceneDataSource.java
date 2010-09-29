/*
 * Copyright (c) 2010 Lockheed Martin Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eurekastreams.server.service.actions.strategies.activity.datasources;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.eurekastreams.commons.logging.LogFactory;
import org.eurekastreams.commons.search.ProjectionSearchRequestBuilder;
import org.hibernate.search.jpa.FullTextQuery;

/**
 * Gets activity IDs from Lucene based on the query.
 */
public class LuceneDataSource implements SortedDataSource
{
    /**
     * Logger.
     */
    private Log log = LogFactory.make();

    /**
     * Search request builder.
     */
    private ProjectionSearchRequestBuilder searchRequestBuilder;

    /**
     * Unstemmed request builder.
     */
    private ProjectionSearchRequestBuilder unstemmedRequestBuilder;

    /**
     * Translates the request to search fields.
     */
    private Map<String, String> requestToField;

    /**
     * Transformers.
     */
    private Map<String, PersistenceDataSourceRequestTransformer> transformers;

    /**
     * Constructor.
     * 
     * @param inSearchRequestBuilder
     *            the search request builder
     * @param inUnstemmedRequestBuilder
     *            the unstemmed request builder.
     * @param inRequestToField
     *            maps requests to Lucene fields.
     * @param inTransformers
     *            the transformers (more than meets the eye).
     */
    public LuceneDataSource(final ProjectionSearchRequestBuilder inSearchRequestBuilder,
            final ProjectionSearchRequestBuilder inUnstemmedRequestBuilder, final Map<String, String> inRequestToField,
            final Map<String, PersistenceDataSourceRequestTransformer> inTransformers)
    {
        searchRequestBuilder = inSearchRequestBuilder;
        unstemmedRequestBuilder = inUnstemmedRequestBuilder;
        requestToField = inRequestToField;
        transformers = inTransformers;
    }

    /**
     * Fetch a page of search results, using the keywords in the input JSON request.
     * 
     * @param inRequest
     *            the JSON request containing query->keywords
     * @param userEntityId
     *            the user entity ID.
     * @return the activity ids
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Long> fetch(final JSONObject inRequest, final Long userEntityId)
    {
        JSONObject jsonQuery = inRequest.getJSONObject("query");

        if (log.isInfoEnabled())
        {
            log.info("JSON Query: " + jsonQuery.toString());
        }

        Boolean hasTerm = false;

        for (String term : requestToField.keySet())
        {
            if (jsonQuery.containsKey(term))
            {
                hasTerm = true;
                break;
            }
        }

        if (!hasTerm
                && (!jsonQuery.containsKey("sortBy") || (jsonQuery.containsKey("sortBy") && jsonQuery.getString(
                        "sortBy").equals("date"))))
        {
            log.info("No search term found");
            return null;
        }

        String luceneQuery = "";
        FullTextQuery query = null;

        for (Entry<String, String> entry : requestToField.entrySet())
        {
            if (jsonQuery.containsKey(entry.getKey()))
            {
                String searchWord = jsonQuery.getString(entry.getKey());
                // remove semicolons, which can be used to search other fields
                searchWord = searchWord.replace(":", "");

                if (transformers.containsKey(entry.getKey()))
                {
                    searchWord = (String) transformers.get(entry.getKey()).transform(jsonQuery, userEntityId);
                }

                luceneQuery += "+" + entry.getValue() + ":(" + searchWord + ")";
            }
        }

        if (log.isInfoEnabled())
        {
            log.info("Lucene Query: " + luceneQuery);
        }

        if (luceneQuery.length() == 0)
        {
            log.info("Returning all activity");
            query = unstemmedRequestBuilder.buildQueryFromNativeSearchString("_hibernate_class:"
                    + "org.eurekastreams.server.domain.stream.Activity");
        }
        else
        {
            query = searchRequestBuilder.buildQueryFromNativeSearchString(luceneQuery);

        }

        if (jsonQuery.containsKey("sortBy"))
        {
            // date
            log.info("Sorting by id, descending");
            query.setSort(new Sort(new SortField(jsonQuery.getString("sortBy"), true)));
        }

        log.debug("Native Lucene Query: " + query.toString());

        searchRequestBuilder.setPaging(query, 0, inRequest.getInt("count"));

        List<Long> activityIds = query.getResultList();
        if (log.isInfoEnabled())
        {
            log.info("Found " + activityIds.size() + " activities");
        }
        return activityIds;
    }
}