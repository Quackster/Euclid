﻿using log4net.Appender;
using log4net.Core;

namespace Euclid.Util.Logging
{
    public class EmptyAppender : ConsoleAppender
    {

        protected override void Append(LoggingEvent loggingEvent)
        {
            // Do nothing
        }
    }
}
