package com.rng350.mediatracker.common

import javax.inject.Inject
import javax.inject.Singleton

// Value of the map points to a Node in the doubly linked list implementation
// K = key
// V = value
@Singleton
class CustomLinkedHashMap<K, V> @Inject constructor() {
    private var _listSize: Int = 0
    val listSize: Int get() = _listSize
    private val hashMap: HashMap<K, Node> = HashMap()
    private var _firstNode: Node? = null
    val firstNode: Node? get() = _firstNode
    private var _lastNode: Node? = null
    val lastNode: Node? get() = _lastNode

    constructor(elements: List<KeyAndValue<K,V>>) : this() {
        elements.forEachIndexed { index, element ->
            val key = element.key
            val currentNode = Node(element)
            hashMap[key] = currentNode
            if (index > 0) {
                currentNode.previousNode = _lastNode
                currentNode.previousNode?.nextNode = currentNode
                _lastNode = currentNode
            }
            else if (index == 0) {
                _firstNode = currentNode
                _lastNode = currentNode
            }
            _listSize++
        }
    }

    constructor(element: KeyAndValue<K, V>) : this() {
        val key = element.key
        val elementNode = Node(element)
        _firstNode = elementNode
        _lastNode = elementNode
        hashMap[key] = elementNode
        _listSize++
    }

    inner class Node(val keyAndValue: KeyAndValue<K,V>) {
        var previousNode: Node? = null
        var nextNode: Node? = null
        val key: K get() = keyAndValue.key
        val value: V get() = keyAndValue.value
    }

    class KeyAndValue<K, V>(val key: K, val value: V)

    fun putAtFrontOfList(key: K, newElement: V) {
        val potentialSameValue = hashMap[key]
        if (potentialSameValue != null) {
            removeNode(potentialSameValue)
        }
        val newFirstNode = Node(KeyAndValue(key, newElement))
        newFirstNode.nextNode = _firstNode
        newFirstNode.previousNode = null
        _firstNode?.previousNode = newFirstNode
        _firstNode = newFirstNode

        hashMap[key] = newFirstNode
        _listSize++
        if (listSize == 1) {
            _lastNode = newFirstNode
        }
    }

    fun putAtEndOfList(key: K, newElement: V) {
        val potentialSameValue = hashMap[key]
        if (potentialSameValue != null) {
            removeNode(potentialSameValue)
        }
        val newLastNode = Node(KeyAndValue(key, newElement))
        newLastNode.previousNode = _lastNode
        newLastNode.nextNode = null
        _lastNode?.nextNode = newLastNode
        _lastNode = newLastNode
        hashMap[key] = newLastNode
        _listSize++
        if (listSize==1) {
            _firstNode = newLastNode
        }
    }

    fun removeFirst(): V? {
        _firstNode?.let {
            val removedNode = removeNode(it)
            return removedNode.value
        }
        return null
    }

    fun removeLast(): V? {
        _lastNode?.let {
            val removedNode = removeNode(it)
            return removedNode.value
        }
        return null
    }

    fun getElement(key: K): V? = hashMap[key]?.value

    fun removeElement(key: K): V? {
        hashMap[key]?.let { node ->
            return removeNode(node).value
        }
        return null
    }

    private fun removeNode(nodeToRemove: Node): Node {
        if (_firstNode?.key == nodeToRemove.key) {
            _firstNode = nodeToRemove.nextNode
        }
        if (_lastNode == nodeToRemove) {
            _lastNode = nodeToRemove.previousNode
        }
        nodeToRemove.previousNode?.nextNode = nodeToRemove.nextNode
        nodeToRemove.nextNode?.previousNode = nodeToRemove.previousNode
        nodeToRemove.previousNode = null
        nodeToRemove.nextNode = null
        hashMap.remove(nodeToRemove.key)
        _listSize--
        return nodeToRemove
    }
}