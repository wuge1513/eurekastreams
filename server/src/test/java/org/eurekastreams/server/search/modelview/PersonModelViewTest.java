/*
 * Copyright (c) 2009-2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.search.modelview;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eurekastreams.commons.search.modelview.ModelView;
import org.eurekastreams.server.search.modelview.PersonModelView.Role;
import org.junit.Before;
import org.junit.Test;

/**
 * Test fixture for PersonModelView.
 */
public class PersonModelViewTest
{
    /**
     * Test data.
     */
    private final Date dateAdded = new Date();

    /**
     * Test data.
     */
    private final String accountId = "sdlkfj23kjsd";

    /**
     * Test data.
     */
    private final String title = "ljksd flksdj ld";

    /**
     * Test data.
     */
    private final String parentOrganizationShortName = "heynow";

    /**
     * Test data.
     */
    private final String parentOrganizationName = "hey now, whoa there";

    /**
     * Test data.
     */
    private final String jobDescription = "Here ya have a guy...";

    /**
     * Test data.
     */
    private final int followersCount = 3838;

    /** Test data. */
    private static final String LAST_NAME = "McNow";

    /** Test data. */
    private static final String PREFERRED_NAME = "Hey";

    /** Test data. */
    private final String displayName = PREFERRED_NAME + " " + LAST_NAME;

    /**
     * The person id.
     */
    private final long personId = 38447L;

    /**
     * Search score.
     */
    private final float searchScore = 775759L;

    /**
     * Person's avatar id.
     */
    private final String avatarId = "lksdlkj23lkjsdfgkh";

    /**
     * The number of updates for this person.
     */
    private final int updatesCount = 28382;

    /**
     * Stream id.
     */
    private final long streamId = 1L;

    /**
     * CompositeStream id.
     */
    private final long compositeStreamId = 2L;

    /**
     * ToS acceptance flag.
     */
    private final boolean tosAcceptance = true;

    /**
     * Flag to determine if person's activities can be commented on.
     */
    private final boolean commentable = false;

    /**
     * Flag to determine if person's stream can be posted on.
     */
    private final boolean streamPostable = false;

    /**
     * Authentication type.
     */
    private final AuthenticationType authenticationType = AuthenticationType.FORM;

    /**
     * IDs of related organizations.
     */
    private List<Long> relatedOrganizationIds;

    /**
     * One of two related org ids for relatedOrganizationIds.
     */
    private final Long relatedOrgId1 = 847L;

    /**
     * One of two related org ids for relatedOrganizationIds.
     */
    private final Long relatedOrgId2 = 999L;

    /** Test data: email address. */
    private static final String EMAIL = "somebody@somewhere.com";

    /**
     * The date the user last accepted terms of service.
     */
    private static Date lastAcceptedTermsOfService = new Date();

    /**
     * Setup method.
     */
    @Before
    public void setup()
    {
        relatedOrganizationIds = new ArrayList<Long>();
        relatedOrganizationIds.add(relatedOrgId1);
        relatedOrganizationIds.add(relatedOrgId2);
    }

    /**
     * Test getEntityName().
     */
    @Test
    public void testGetEntityName()
    {
        assertEquals("Person", new PersonModelView().getEntityName());
    }

    /**
     * Test the properties.
     */
    @Test
    public void testProperties()
    {
        PersonModelView sut = new PersonModelView();

        // check initial state
        assertDefaultValues(sut);

        // set properties
        sut.setDateAdded(dateAdded);
        sut.setAccountId(accountId);
        sut.setTitle(title);
        sut.setParentOrganizationName(parentOrganizationName);
        sut.setParentOrganizationShortName(parentOrganizationShortName);
        sut.setDescription(jobDescription);
        sut.setFollowersCount(followersCount);
        sut.setLastName(LAST_NAME);
        sut.setPreferredName(PREFERRED_NAME);
        sut.setDisplayName(displayName);
        sut.setAvatarId(avatarId);
        sut.setUpdatesCount(updatesCount);
        sut.setStreamId(streamId);
        sut.setTosAcceptance(tosAcceptance);
        sut.setAuthenticationType(authenticationType);
        sut.setEmail(EMAIL);
        sut.setCommentable(commentable);
        sut.setStreamPostable(streamPostable);
        sut.setRelatedOrganizationIds(relatedOrganizationIds);
        sut.setLastAcceptedTermsOfService(lastAcceptedTermsOfService);
        sut.setAccountLocked(true);

        // check new state
        assertAll(sut);
    }

    /**
     * Test loading an empty property map keeps default values.
     */
    @Test
    public void testLoadEmptyProperties()
    {
        HashMap<String, Object> p = new HashMap<String, Object>();
        PersonModelView sut = new PersonModelView();
        sut.loadProperties(p);
        assertDefaultValues(sut);
    }

    /**
     * Test loading all properties.
     */
    @Test
    public void testLoadProperties()
    {
        HashMap<String, Object> p = new HashMap<String, Object>();
        p.put("__HSearch_id", personId);
        p.put("__HSearch_Score", searchScore);
        p.put("dateAdded", dateAdded);
        p.put("accountId", accountId);
        p.put("title", title);
        p.put("parentOrganizationName", parentOrganizationName);
        p.put("parentOrganizationShortName", parentOrganizationShortName);
        p.put("jobDescription", jobDescription);
        p.put("preferredName", "Hey");
        p.put("lastName", "McNow");
        p.put("followersCount", followersCount);
        p.put("updatesCount", updatesCount);
        p.put("avatarId", avatarId);
        p.put("updatesCount", updatesCount);
        p.put("streamId", streamId);
        p.put("compositeStreamId", compositeStreamId);
        p.put("tosAcceptance", tosAcceptance);
        p.put("authenticationType", authenticationType);
        p.put("email", EMAIL);
        p.put("commentable", commentable);
        p.put("streamPostable", streamPostable);
        p.put("relatedOrganizationIds", relatedOrganizationIds);
        p.put("lastAcceptedTermsOfService", lastAcceptedTermsOfService);
        p.put("accountLocked", Boolean.TRUE);

        PersonModelView sut = new PersonModelView();

        // test default
        assertDefaultValues(sut);

        // load the properties
        sut.loadProperties(p);

        HashSet<Role> roles = new HashSet<Role>();
        sut.setRoles(roles);
        assertEquals(roles, sut.getRoles());

        // assert the properties
        assertAll(sut);

        // check search score as well
        assertEquals(searchScore, sut.getSearchIndexScore(), 0);
    }

    /**
     * Test helper method to assert the default values.
     *
     * @param sut
     *            the SUT
     */
    private void assertDefaultValues(final PersonModelView sut)
    {
        assertEquals(ModelView.UNINITIALIZED_DATE_VALUE, sut.getDateAdded());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getAccountId());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getTitle());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getParentOrganizationName());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getParentOrganizationShortName());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getDescription());
        assertEquals(ModelView.UNINITIALIZED_INTEGER_VALUE, sut.getFollowersCount());
        assertEquals(ModelView.UNINITIALIZED_FLOAT_VALUE, sut.getSearchIndexScore(), 0);
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getLastName());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getPreferredName());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getDisplayName());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getAvatarId());
        assertEquals(ModelView.UNINITIALIZED_INTEGER_VALUE, sut.getUpdatesCount());
        assertEquals(ModelView.UNINITIALIZED_LONG_VALUE, sut.getStreamId());
        assertEquals(ModelView.UNINITIALIZED_DATE_VALUE, sut.getLastAcceptedTermsOfService());
        assertEquals(false, sut.getTosAcceptance());
        assertEquals(AuthenticationType.NOTSET, sut.getAuthenticationType());
        assertEquals(ModelView.UNINITIALIZED_STRING_VALUE, sut.getEmail());
        assertEquals(true, sut.isCommentable());
        assertEquals(true, sut.isStreamPostable());
        assertNull(sut.getRelatedOrganizationIds());
        assertFalse(sut.isAccountLocked());
    }

    /**
     * Test helper method to assert all properties.
     *
     * @param sut
     *            the SUT
     */
    private void assertAll(final PersonModelView sut)
    {
        assertEquals(dateAdded, sut.getDateAdded());
        assertEquals(accountId, sut.getAccountId());
        assertEquals(title, sut.getTitle());
        assertEquals(parentOrganizationName, sut.getParentOrganizationName());
        assertEquals(parentOrganizationShortName, sut.getParentOrganizationShortName());
        assertEquals(jobDescription, sut.getDescription());
        assertEquals(followersCount, sut.getFollowersCount());
        assertEquals(LAST_NAME, sut.getLastName());
        assertEquals(PREFERRED_NAME, sut.getPreferredName());
        assertEquals(displayName, sut.getDisplayName());
        assertEquals(avatarId, sut.getAvatarId());
        assertEquals(updatesCount, sut.getUpdatesCount());
        assertEquals(streamId, sut.getStreamId());
        assertEquals(tosAcceptance, sut.getTosAcceptance());
        assertEquals(authenticationType, sut.getAuthenticationType());
        assertEquals(EMAIL, sut.getEmail());
        assertEquals(commentable, sut.isCommentable());
        assertEquals(streamPostable, sut.isStreamPostable());
        assertEquals(lastAcceptedTermsOfService, sut.getLastAcceptedTermsOfService());
        assertArrayEquals(relatedOrganizationIds.toArray(), sut.getRelatedOrganizationIds().toArray());

        assertEquals(sut.getEntityId(), sut.getId());
        assertEquals(sut.getAccountId(), sut.getUniqueId());
        assertTrue(sut.isAccountLocked());
    }
}
