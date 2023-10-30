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

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.ErrorProneFlags;
import com.google.errorprone.bugpatterns.BugChecker;

import javax.inject.Inject;

@AutoService(BugChecker.class)
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
        this(ErrorProneFlags.empty());
    }

    @Inject
    public TrinoExperimentalSpiChecker(ErrorProneFlags flags)
    {
        super(
                "io.trino.spi.Experimental",
                flags.getSetOrEmpty("TrinoExperimentalSpi:BasePackages"),
                flags.getSetOrEmpty("TrinoExperimentalSpi:IgnoredPackages"),
                flags.getSetOrEmpty("TrinoExperimentalSpi:IgnoredTypes"));
    }

    @Override
    protected String messageForMatchingBasePackage(String packageName)
    {
        return "Do not use Trino @Experimental SPIs from '%s'.".formatted(packageName);
    }
}
