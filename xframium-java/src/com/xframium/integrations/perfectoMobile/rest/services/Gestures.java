package com.xframium.integrations.perfectoMobile.rest.services;

import java.awt.Point;
import com.xframium.integrations.common.PercentagePoint;
import com.xframium.integrations.perfectoMobile.rest.bean.Execution;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Gestures.
 */
@ServiceDescriptor ( serviceName = "executions")
public interface Gestures extends PerfectoService
{

    @Operation ( operationName = "command")
    @PerfectoCommand ( commandName = "touch", subCommandName = "tap")
    public Execution tap( @ResourceID String executionId, @Parameter ( name = "handsetId" ) String handsetId, @Parameter ( name = "location") PercentagePoint location);

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
