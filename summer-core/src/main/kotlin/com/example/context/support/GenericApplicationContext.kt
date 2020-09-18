package com.example.context.support

import com.example.beans.factory.annotation.Autowired
import com.example.beans.factory.annotation.Component
import org.reflections.Reflections
import kotlin.reflect.KClass

class GenericApplicationContext : ApplicationContext {

    private val beans = mutableMapOf<String, Any?>()
    private val reflections = Reflections("com.example")

    override fun <T : Any> getBean(clazz: KClass<T>): T {
        val beanName = clazz.java.canonicalName
        val result = findBean(beanName) ?: createBean(beanName)
        return result as T
    }

    private fun findBean(beanName: String): Any? = beans[beanName]

    private fun createBean(beanName: String): Any? {
        val implementations =
            reflections.getSubTypesOf(Class.forName(beanName)).filter { it.isAnnotationPresent(Component::class.java) }

        assert(implementations.size == 1) { "У интерфейса $beanName не найдена основная реализация!" }
        val implementation = implementations.first()
        val constructors =
            implementation.constructors.filter { it.isAnnotationPresent(Autowired::class.java) || it.parameters.isEmpty() }

        assert(constructors.size == 1) {"В классе ${implementation.simpleName} не найден подходящий конструктор!" }
        val constructor = constructors.first()

        val constructorArgs = constructor.parameters.map {
            getBean(Class.forName(it.type.name).kotlin)
        }

        beans[beanName] = constructor.newInstance(*constructorArgs.toTypedArray())

        return beans[beanName]
    }
}
