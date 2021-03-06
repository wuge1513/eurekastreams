/*
 * Copyright (c) 2010-2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.action.execution.notification.translator;

import org.eurekastreams.server.action.execution.notification.NotificationBatch;
import org.eurekastreams.server.action.execution.notification.NotificationPropertyKeys;
import org.eurekastreams.server.action.request.notification.TargetEntityNotificationsRequest;
import org.eurekastreams.server.domain.EntityType;
import org.eurekastreams.server.domain.NotificationType;
import org.eurekastreams.server.persistence.mappers.DomainMapper;
import org.eurekastreams.server.search.modelview.PersonModelView;
import org.eurekastreams.server.service.utility.ui.UiUrlBuilder;

/**
 * Translates the event of someone beginning to follow a stream to appropriate notifications.
 */
public class FollowPersonTranslator implements NotificationTranslator<TargetEntityNotificationsRequest>
{
    /** DAO to get the person's account id. */
    private final DomainMapper<Long, String> idToUniqueIdDAO;

    /**
     * Constructor.
     *
     * @param inIdToUniqueIdDAO
     *            DAO to get the person's account id.
     */
    public FollowPersonTranslator(final DomainMapper<Long, String> inIdToUniqueIdDAO)
    {
        idToUniqueIdDAO = inIdToUniqueIdDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationBatch translate(final TargetEntityNotificationsRequest inRequest)
    {
        NotificationBatch batch = new NotificationBatch(NotificationType.FOLLOW_PERSON, inRequest.getTargetEntityId());
        batch.setProperty(NotificationPropertyKeys.ACTOR, PersonModelView.class, inRequest.getActorId());
        batch.setProperty("stream", PersonModelView.class, inRequest.getTargetEntityId());
        batch.setPropertyAlias(NotificationPropertyKeys.SOURCE, "stream");

        batch.setProperty(NotificationPropertyKeys.URL, UiUrlBuilder.relativeUrlForEntity(EntityType.PERSON,
                idToUniqueIdDAO.execute(inRequest.getActorId())));

        return batch;
    }
}
