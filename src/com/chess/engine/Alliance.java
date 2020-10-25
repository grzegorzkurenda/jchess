package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {

	WHITE{
		@Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }
		
		@Override
		public int getDirection() {	
			return -1;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return whitePlayer;
		}

		@Override
		public int getOppositeDirection() {
			return 1;
		}

		@Override
		public boolean isPawnPromotionSquare(int position) {
			if(position>=0&&position<8) {return true;}
			return false;
		}
	},
	BLACK{
		@Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }
		
		@Override
		public int getDirection() {
			return 1;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		@Override
		public int getOppositeDirection() {
			return -1;
		}

		@Override
		public boolean isPawnPromotionSquare(int position) {
			if(position>=56&&position<64) {return true;}
			return false;
		}
	};
	public abstract int getDirection();
	public abstract boolean isBlack();
	public abstract boolean isWhite();
	public abstract boolean isPawnPromotionSquare(int position);
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
	public abstract int getOppositeDirection();

}
