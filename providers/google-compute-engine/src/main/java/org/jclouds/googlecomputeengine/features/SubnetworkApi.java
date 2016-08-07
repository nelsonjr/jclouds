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
package org.jclouds.googlecomputeengine.features;

import com.google.common.base.Function;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.googlecloud.domain.ListPage;
import org.jclouds.googlecomputeengine.GoogleComputeEngineApi;
import org.jclouds.googlecomputeengine.compute.loaders.SubnetworkLoader;
import org.jclouds.googlecomputeengine.domain.Operation;
import org.jclouds.googlecomputeengine.domain.Subnetwork;
import org.jclouds.googlecomputeengine.internal.BaseCallerArg0ToIteratorOfListPage;
import org.jclouds.googlecomputeengine.internal.BaseToIteratorOfListPage;
import org.jclouds.googlecomputeengine.options.ListOptions;
import org.jclouds.googlecomputeengine.options.SubnetworkCreationOptions;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.oauth.v2.filters.OAuthFilter;
import org.jclouds.rest.annotations.*;
import org.jclouds.rest.binders.BindToJsonPayload;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import java.net.URI;
import java.util.Iterator;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@SkipEncoding({'/', '='})
@RequestFilters(OAuthFilter.class)
@Path("/subnetworks")
@Consumes(APPLICATION_JSON)
public interface SubnetworkApi {

   /**
    * Returns a network by name or null if not found.
    */
   @Named("Subnetworks:get")
   @GET
   @Path("/{subnetwork}")
   @Fallback(NullOnNotFoundOr404.class)
   Subnetwork get(@PathParam("subnetwork") String subnetworkName);

//   /**
//    * Creates a persistent network resource in the specified project with the specified range.
//    *
//    * @param networkName the network name
//    * @param IPv4Range   the range of the network to be inserted.
//    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
//    *         you, and look for the status field.
//    */
//   @Named("Networks:insert")
//   @POST
//   @Produces(APPLICATION_JSON)
//   @MapBinder(BindToJsonPayload.class)
//   Operation createInIPv4Range(@PayloadParam("name") String networkName,
//                               @PayloadParam("IPv4Range") String IPv4Range);

//   /**
//    * Creates a persistent network resource in the specified project with the specified range and specified gateway.
//    *
//    * @param options definition of the subnetwork.
//    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
//    *         you, and look for the status field.
//    */
//   @Named("Networks:insert")
//   @POST
//   @Produces(APPLICATION_JSON)
//   Operation createInIPv4Range(@BinderParam(BindToJsonPayload.class) SubnetworkCreationOptions options);

//   /** Deletes a network by name and returns the operation in progress, or null if not found. */
//   @Named("Networks:delete")
//   @DELETE
//   @Path("/{network}")
//   @Fallback(NullOnNotFoundOr404.class)
//   Operation delete(@PathParam("network") String networkName);

   /**
    * Retrieves the list of network resources available to the specified project.
    * By default the list as a maximum size of 100, if no options are provided or ListOptions#getMaxResults() has not
    * been set.
    *
    * @param pageToken   marks the beginning of the next list page
    * @param listOptions listing options
    * @return a page of the list
    */
   @Named("Subnetworks:list")
   @GET
   ListPage<Subnetwork> listPage(@Nullable @QueryParam("pageToken") String pageToken, ListOptions listOptions);

   /** @see #listPage(String, ListOptions) */
   @Named("Networks:list")
   @GET
   @Transform(SubnetworkPages.class)
   Iterator<ListPage<Subnetwork>> list();

   static final class SubnetworkPages extends BaseCallerArg0ToIteratorOfListPage<Subnetwork, SubnetworkPages> {

      private final GoogleComputeEngineApi api;

      @Inject SubnetworkPages(GoogleComputeEngineApi api) {
         this.api = api;
      }

      @Override protected Function<String, ListPage<Subnetwork>> fetchNextPage(final String region, final ListOptions options) {
         return new Function<String, ListPage<Subnetwork>>() {
            @Override public ListPage<Subnetwork> apply(String pageToken) {
               return api.subnetworksInRegion(region).listPage(pageToken, options);
            }
         };
      }
   }
}
