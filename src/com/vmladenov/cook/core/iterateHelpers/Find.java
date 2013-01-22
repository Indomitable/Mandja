/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.core.iterateHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Find {

    public static <T> T firstOrDefault(Iterable<T> iterable, IPredicate<T> predicate) {
        Iterator<T> iterator = iterable.iterator();
        while (iterable.iterator().hasNext()) {
            T next = iterable.iterator().next();
            if (predicate.predicate(next))
                return next;
        }
        return null;
    }

    public static <T> Collection<T> where(Iterable<T> iterable, IPredicate<T> predicate){
        ArrayList<T> list = new ArrayList<T>();
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (predicate.predicate(next))
                list.add(next);
        }
        return list;
    }
}
