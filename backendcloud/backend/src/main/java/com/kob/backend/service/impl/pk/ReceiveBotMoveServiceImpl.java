package com.kob.backend.service.impl.pk;

import cn.hutool.core.util.ObjectUtil;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction, Integer opponentId) {
        System.out.println("receive bot move: " + userId + " " + direction);
        if (ObjectUtil.isNotNull(WebSocketServer.users.get(userId)) || userId.equals(0)) {
            Game game = null;
            if(userId.equals(0)) {
                game = WebSocketServer.users.get(opponentId).game;
            } else {
                game = WebSocketServer.users.get(userId).game;
            }
            if (ObjectUtil.isNotNull(game)) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }
        return "receive bot move success";
    }
}
