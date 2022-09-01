/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package io.trino.spi.trinoexperimentalspi;

import io.trino.spi.Experimental;

public class ClassWithAnnotatedMember
{
    @Experimental
    public void annotated()
    {}

    public void notAnnotated()
    {}
}
