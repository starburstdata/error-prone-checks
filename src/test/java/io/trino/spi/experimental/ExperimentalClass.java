/*
 * Copyright Starburst Data, Inc. All rights reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF STARBURST DATA.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 *
 * Redistribution of this material is strictly prohibited.
 */

package io.trino.spi.experimental;

import io.trino.spi.Experimental;

@SuppressWarnings("unused")
@Experimental
public class ExperimentalClass
{
    public static final String STATIC_MEMBER = "dummy";

    public void foo()
    {}
}
