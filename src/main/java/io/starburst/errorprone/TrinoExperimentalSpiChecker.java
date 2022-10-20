/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package io.starburst.errorprone;

import com.google.errorprone.BugPattern;

@BugPattern(
        name = "TrinoExperimentalSpi",
        summary = "Do not use Trino @Experimental SPIs.",
        explanation = """
                Trino SPIs marked as @Experimental should be avoided \
                in libraries and shared components that other projects depend on.""",
        linkType = BugPattern.LinkType.NONE,
        severity = BugPattern.SeverityLevel.WARNING)
public final class TrinoExperimentalSpiChecker
        extends AnnotatedApiUsageChecker
{
    public TrinoExperimentalSpiChecker()
    {
        super("io.trino.spi", "io.trino.spi.Experimental");
    }
}
