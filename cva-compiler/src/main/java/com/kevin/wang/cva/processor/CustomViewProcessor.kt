package com.kevin.wang.cva.processor

import com.google.common.base.CaseFormat
import com.kevin.wang.cva.annotations.CustomView
import com.squareup.javapoet.*
import jdk.internal.org.objectweb.asm.Type
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@SupportedAnnotationTypes("com.kevin.wang.cva.annotations.CustomView")
class CustomViewProcessor : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var elems: Elements
    private lateinit var types: Types

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)

        messager = processingEnv.messager
        elems = processingEnv.elementUtils
        types = processingEnv.typeUtils
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val viewgroupType = typeMirror("android.view.View")
        val elementList = roundEnvironment.getElementsAnnotatedWith(CustomView::class.java)
        val elements = elementList.toSet()

        //Binding error checking
        for (element in elementList) {
            if (element.kind != ElementKind.CLASS || !types.isSubtype(element.asType(), viewgroupType)) {
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("Only view classes can be annotated \n%s is not a child class of android.view.View", element.asType()))
                return true
            }
        }

        if (elements.size != 0) {

            val factoryClass = brewFactoryClass(elements)

            JavaFile.builder(packageOf(elements.first()).toString(), factoryClass)
                    .indent("    ")
                    .build()
                    .writeTo(processingEnv.filer)

        }

        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun brewFactoryClass(elements: Set<Element>): TypeSpec{
        val context = typeName("android.content.Context")
        val view = typeName("android.view.View")
        val string = typeName("java.lang.String")
        val attributeSet = typeName("android.util.AttributeSet")
        val xmlPullParser = typeName("org.xmlpull.v1.XmlPullParser")
        val viewParent = typeName("android.view.ViewParent")
        val viewCompat = typeName("androidx.core.view.ViewCompat")
        val nameMap = mutableMapOf<String, String>()

        val inflaterClass = TypeSpec.classBuilder("CustomInflaterFactory")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(typeName("android.view.LayoutInflater.Factory2"))

        for (element in elements){
            nameMap += element.className() to element.className().toConstantCase() //name of the method
            val constField = FieldSpec.builder(string, nameMap[element.className()])
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer(""""${element.fullyQualifiedName()}"""")
                    .build()

            inflaterClass.addField(constField)
        }

        val createView1 = MethodSpec.methodBuilder("onCreateView")
                .addJavadoc("""Main factory method. Feel feel to override this to provide a custom implementation.${'\n'}""")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(view)
                .addParameter(view, "view")
                .addParameter(string, "name")
                .addParameter(context, "context")
                .addParameter(attributeSet, "attributeSet")
                .addNamedCode("${'$'}view:T result = null;\n\n", mapOf("view" to view))
                .addCode("switch(name) {\n")
                .apply {
                    for (element in elements){
                        addCode("""
                                |    case(${nameMap[element.className()]}): {
                                |        result = ${viewCreatorName(element)}(context, attributeSet);
                                |        verifyNotNull(result, ${nameMap[element.className()]});
                                |        break;
                                |    }${'\n'}
                            """.trimMargin())
                    }
                }
                .addCode("}\n\n")
                .addCode("return result;\n")
                .build()
        inflaterClass.addMethod(createView1)

        val createView2 = MethodSpec.methodBuilder("onCreateView")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(view)
                .addParameter(string, "s")
                .addParameter(context, "context")
                .addParameter(attributeSet, "attributeSet")
                .addCode("""return onCreateView(null, s, context, attributeSet);""")
                .addCode("\n")
                .build()
        inflaterClass.addMethod(createView2)

        val verifyNotNull = MethodSpec.methodBuilder("verifyNotNull")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(view, "view")
                .addParameter(string, "name")
                .addCode("""
                        if (view == null) {
                            throw new IllegalStateException(this.getClass().getName()
                                 + " asked to inflate view for <" + name + ">, but returned null");
                        }
                    """.trimIndent())
                .addCode("\n")
                .build()
        inflaterClass.addMethod(verifyNotNull)

        for (element in elements){
            val viewType = typeName(element.asType().toString())
            val viewCreatorMethod = MethodSpec.methodBuilder(viewCreatorName(element))
                    .addModifiers(Modifier.PROTECTED)
                    .returns(viewType)
                    .addParameter(context, "context")
                    .addParameter(typeName("android.util.AttributeSet"), "attrs")
                    .addNamedCode("return new ${'$'}view:T(context, attrs);", mapOf(
                            "view" to viewType
                    )).addCode("\n")
                    .build()
            inflaterClass.addMethod(viewCreatorMethod)
        }

        return inflaterClass.build();
    }

    private fun typeMirror(str: String): TypeMirror = elems.getTypeElement(str).asType()

    private fun typeName(str: String): TypeName = TypeName.get(elems.typeMirror(str))

    private fun packageOf(element: Element) = elems.getPackageOf(element)

    private fun viewCreatorName(element: Element) = "create${element.simpleName}"
}

private fun Elements.typeMirror(str: String): TypeMirror {
    return getTypeElement(str).asType()
}

private fun Element.className(): String = simpleName.toString()

private fun Element.fullyQualifiedName(): String = asType().toString()

private fun String.toConstantCase(): String = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this)