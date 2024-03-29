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
        name = "DeprecatedApi",
        summary = "Do not use @Deprecated APIs.",
        explanation = """
                APIs marked as @Deprecated should be avoided \
                in libraries and shared components that other projects depend on.""",
        linkType = BugPattern.LinkType.NONE,
        severity = BugPattern.SeverityLevel.WARNING)
public final class DeprecatedApiChecker
        extends AnnotatedApiUsageChecker
{
    public DeprecatedApiChecker()
    {
        this(ErrorProneFlags.empty());
    }

    @Inject
    public DeprecatedApiChecker(ErrorProneFlags flags)
    {
        super(
                "java.lang.Deprecated",
                flags.getSetOrEmpty("DeprecatedApi:BasePackages"),
                flags.getSetOrEmpty("DeprecatedApi:IgnoredPackages"),
                flags.getSetOrEmpty("DeprecatedApi:IgnoredTypes"));
    }

    @Override
    protected String messageForMatchingBasePackage(String packageName)
    {
        return "Do not use @Deprecated APIs from '%s'.".formatted(packageName);
    }
}
