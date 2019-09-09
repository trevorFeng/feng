package com.trevor.message.core.event.niuniu;

import com.trevor.message.bo.RoomData;
import com.trevor.message.bo.Task;
import com.trevor.message.core.event.BaseEvent;
import com.trevor.message.core.event.Event;
import org.springframework.stereotype.Service;

@Service
public class CountDownEvent extends BaseEvent implements Event {


    @Override
    public void execute(RoomData roomData, Task task) {

    }
}
