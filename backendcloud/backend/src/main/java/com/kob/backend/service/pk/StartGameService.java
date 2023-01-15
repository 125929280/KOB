package com.kob.backend.service.pk;

import java.util.concurrent.ExecutionException;

public interface StartGameService {
    String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) throws ExecutionException, InterruptedException;
}
