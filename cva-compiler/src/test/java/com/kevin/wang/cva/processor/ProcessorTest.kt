package com.kevin.wang.cva.processor

import com.kevin.wang.cva.annotations.CustomView
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class ProcessorTest {

    companion object {

    }

    @Mock lateinit var processingEnvironment: ProcessingEnvironment
    @Mock lateinit var filer: Filer
    @Mock lateinit var elementUtils: Elements
    @Mock lateinit var messager: Messager
    @Mock lateinit var typeUtils: Types
    @Mock lateinit var roundEnvironment: RoundEnvironment

    private val processor = CustomViewProcessor()

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        `when`(processingEnvironment.filer).thenReturn(filer)
        `when`(processingEnvironment.elementUtils).thenReturn(elementUtils)
        `when`(processingEnvironment.messager).thenReturn(messager)
        `when`(processingEnvironment.typeUtils).thenReturn(typeUtils)

        `when`(roundEnvironment.getElementsAnnotatedWith(eq(CustomView::class.java))).thenReturn(setOf(createElement("a"), createElement("b"), createElement("c")))

        processor.init(processingEnvironment)
    }

    @Test
    fun validation(){
        processor.process(setOf(), roundEnvironment)
        assertTrue(true)
    }
}

private fun ProcessorTest.createElement(name: String): Element {
    val element = mock(Element::class.java)
    val _name = mock(Name::class.java)
    `when`(_name.toString()).thenReturn(name)
    `when`(element.simpleName).thenReturn(_name)

    return element
}