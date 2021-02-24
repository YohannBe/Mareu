package com.example.mareu.Utils;


import android.view.View;

import androidx.annotation.Nullable;
import androidx.test.espresso.core.internal.deps.guava.base.Predicate;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.core.internal.deps.guava.collect.Lists;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class WithViewCount {


    public static Matcher<View> withViewCount(final Matcher<View> viewMatcher, final int expectedCount) {
        return new TypeSafeMatcher<View>() {
            int actualCount = -1;

            @Override
            public void describeTo(Description description) {
                if (actualCount >= 0) {
                    description.appendText("With expected number of items: " + expectedCount);
                    description.appendText("\n With matcher: ");
                    viewMatcher.describeTo(description);
                    description.appendText("\n But got: " + actualCount);
                }
            }

            @Override
            protected boolean matchesSafely(View root) {
                actualCount = 0;
                Iterable<View> iterable = TreeIterables.breadthFirstViewTraversal(root);
                actualCount = Lists.newArrayList(Iterables.filter(iterable, withMatcherPredicate(viewMatcher))).size();
                return actualCount == expectedCount;
            }
        };
    }

    private static Predicate<View> withMatcherPredicate(final Matcher<View> matcher) {
        return new Predicate<View>() {
            @Override
            public boolean apply(@Nullable View view) {
                return matcher.matches(view);
            }
        };
    }
}