package com.maksimovamaris.chess.presenter;

import java.util.Date;

public interface RestoreGameView {
    void onGameRestored(Date date, String human, String bot);
}
