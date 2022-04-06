package controllers;

import date.Node;
import model.TaskBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//реализация класса менеджер истории


public class InMemoryHistoryManager implements HistoryTaskManager {
    private LinkedList<TaskBase> historyList = new LinkedList<>();
    private HashMap<Integer, Node> listMap = new HashMap<>();


    @Override
    public void addTask(TaskBase task) {
        if(task == null) throw new IllegalArgumentException("передан null");
        if (listMap.containsKey(task.getId())) {
            removeTaskInHistory(task);
        }
        historyList.add(task, task.getId());
    }

    @Override
    public List<TaskBase> getHistory() {
        return historyList.getTasks();
    }

    public void removeTaskInHistory(TaskBase task) {
        historyList.removeNode(listMap.get(task.getId()));
        listMap.remove(task.getId());
    }

    // класс списка для хранения истории просмотра
    class LinkedList<TaskBase> {
        private Node<TaskBase> head;
        private Node<TaskBase> tail;

        //добавление в список
        private void add(TaskBase task, int id) {

            Node<TaskBase> newNode = new Node<>(task);
            listMap.put(id, newNode);
            if (head == null) {
                head = newNode;
            } else {
                tail.setNext(newNode);
                newNode.setPrev(tail);
            }
            tail = newNode;
        }

        //возвращение истории просмотра
        private List<TaskBase> getTasks() {
            List<TaskBase> tasks = new ArrayList<>();
            Node<TaskBase> node = null;
            for (int i = 0; i < listMap.size(); i++) {
                if (i == 0) {
                    tasks.add(head.getDate());
                    node = head.getNext();
                } else {
                    tasks.add(node.getDate());
                    node = node.getNext();
                }
            }
            return tasks;
        }

        //удаление из списка
        private void removeNode(Node node) {
            if (node == null) {
                return;
            }
            if (node == head) {
                if (head.getNext() != null) {
                    head.getNext().setPrev(null);
                    head = head.getNext();
                } else {
                    head = null;
                }
            } else if (node == tail) {
                tail.getPrev().setNext(null);
                tail = tail.getPrev();
            } else {
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            }
        }
    }
}