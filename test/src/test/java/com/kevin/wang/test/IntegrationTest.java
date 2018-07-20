package com.kevin.wang.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.kevin.wang.cva.annotations.CustomView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class IntegrationTest {

    @Mock
    AttributeSet attributeSet = mock(AttributeSet.class);

    private Context context;
    private CustomInflaterFactory customInflaterFactory;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        customInflaterFactory = new CustomInflaterFactory();
    }

    @Test
    public void test() {
        assertNotNull(customInflaterFactory.createCustomView1(context, attributeSet));
        assertNotNull(customInflaterFactory.createCustomView2(context, attributeSet));
        assertTrue(customInflaterFactory.onCreateView(mock(View.class), CustomView1.class.getName(), context, attributeSet) instanceof CustomView1);
        assertTrue(customInflaterFactory.onCreateView(mock(View.class), CustomView2.class.getName(), context, attributeSet) instanceof CustomView2);
        assertTrue(customInflaterFactory.onCreateView(CustomView1.class.getName(), context, attributeSet) instanceof CustomView1);
        assertTrue(customInflaterFactory.onCreateView(CustomView2.class.getName(), context, attributeSet) instanceof CustomView2);
    }

}
