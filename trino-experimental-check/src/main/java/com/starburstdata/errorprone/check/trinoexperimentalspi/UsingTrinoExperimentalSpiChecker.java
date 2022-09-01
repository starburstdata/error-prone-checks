/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package com.starburstdata.errorprone.check.trinoexperimentalspi;

import com.google.errorprone.BugPattern;

@BugPattern(
        name = "UsingTrinoExperimentalSpi",
        summary = "Do not use Trino experimental SPI.",
        explanation = """
                Using Trino SPIs marked as experimental should be avoided \
                in libraries and shared components that other projects depend on.""",
        linkType = BugPattern.LinkType.NONE,
        severity = BugPattern.SeverityLevel.WARNING)
public final class UsingTrinoExperimentalSpiChecker
        extends AnnotatedApiUsageChecker
{
    public UsingTrinoExperimentalSpiChecker()
    {
        super("io.trino.spi", "io.trino.spi.Experimental");
    }
}
