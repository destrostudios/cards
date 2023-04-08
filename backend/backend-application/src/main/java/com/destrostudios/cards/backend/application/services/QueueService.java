package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.database.databases.QueryResult;
import com.destrostudios.cards.shared.model.Queue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class QueueService {

    public QueueService(Database database) {
        this.database = database;
        queues = new HashMap<>();
        loadQueues();
    }
    private Database database;
    private HashMap<Integer, Queue> queues;

    public void loadQueues() {
        try (QueryResult result = database.select("SELECT * FROM queue")) {
            while (result.next()) {
                int queueId = result.getInteger("id");
                Queue queue = new Queue(
                    queueId,
                    result.getString("name")
                );
                queues.put(queue.getId(), queue);
            }
        }
    }

    public List<Queue> getQueues() {
        return new LinkedList<>(queues.values());
    }

    public Queue getQueue(int queueId) {
        return queues.get(queueId);
    }

    public Queue getQueue(String name) {
        return queues.values().stream().filter(queue -> queue.getName().equals(name)).findFirst().orElseThrow();
    }
}
