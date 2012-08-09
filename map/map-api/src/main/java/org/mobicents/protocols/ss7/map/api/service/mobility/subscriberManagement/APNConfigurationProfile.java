/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * 

APN-ConfigurationProfile ::= SEQUENCE {
	defaultContext	ContextId,
	completeDataListIncluded	NULL			OPTIONAL,
		-- If segmentation is used, completeDataListIncluded may only be present in the
		-- first segment of APN-ConfigurationProfile.
	epsDataList	[1]	EPS-DataList,
	extensionContainer	[2] ExtensionContainer	OPTIONAL,
	... }

ContextId ::= INTEGER (1..50)

EPS-DataList ::= SEQUENCE SIZE (1..50) OF APN-Configuration

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface APNConfigurationProfile {

	public int getDefaultContext();

	public boolean getCompleteDataListIncluded();

	public ArrayList<APNConfiguration> getEPSDataList();

	public MAPExtensionContainer getExtensionContainer();

}