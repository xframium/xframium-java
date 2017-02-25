/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.integrations.perfectoMobile.rest.services;

import org.xframium.integrations.common.PercentagePoint;
import org.xframium.integrations.perfectoMobile.rest.bean.Execution;
import org.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Gestures.
 */
@ServiceDescriptor ( serviceName = "executions")
public interface Gestures extends PerfectoService
{

    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "tap")
    public Execution tap( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "location") PercentagePoint location, @Parameter ( name = "duration") int duration);

    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "tap")
    public Execution tapAt( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "location") PercentagePoint location);

    /**
     * Swipe.
     *
     * @param executionId
     *            the execution id
     * @param handsetId
     *            the handset id
     * @param start
     *            the start
     * @param end
     *            the end
     * @return the execution
     */
    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "swipe")
    public Execution swipe( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "start") PercentagePoint start, @Parameter ( name = "end") PercentagePoint end);
    
    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "swipe")
    public Execution swipe( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "start") PercentagePoint start, @Parameter ( name = "end") PercentagePoint end, @Parameter ( name = "duration") Integer duration);

    /**
     * Zoom.
     *
     * @param executionId
     *            the execution id
     * @param handsetId
     *            the handset id
     * @param start
     *            the start
     * @param end
     *            the end
     * @return the execution
     */
    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "gesture")
    public Execution zoom( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "start") PercentagePoint start, @Parameter ( name = "end") PercentagePoint end);

    /**
     * Pinch.
     *
     * @param executionId
     *            the execution id
     * @param handsetId
     *            the handset id
     * @param start
     *            the start
     * @param end
     *            the end
     * @return the execution
     */
    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "gesture")
    public Execution pinch( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "start") PercentagePoint start, @Parameter ( name = "end") PercentagePoint end);

    /**
     * Swipe.
     *
     * @param executionId
     *            the execution id
     * @param handsetId
     *            the handset id
     * @param key
     *            the key
     * @param metastate
     *            the metastate
     * @return the execution
     */
    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "key", subCommandName = "event")
    public Execution sendKey( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "key") Integer key, @Parameter ( name = "metastate") Integer metastate);

}
