package com.chess.engine.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtils {
	public static final String[] AlGEBREIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String,Integer> POSITION_TO_CORD = initializePositionToCoordinateMap();
	
	private BoardUtils() throws Exception {
		throw new Exception("You can not instantiate me");
	}

	

	public static boolean isValidTileCord(int candidateCord) {
		return candidateCord>-1&&candidateCord<64;
	}
	
	public static int getCordPosition(String position) {	
		return POSITION_TO_CORD.get(position);
	}
	
	public static String getPositionCord(int destinationCord) {	
		return AlGEBREIC_NOTATION[destinationCord];
	}
	
	private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            positionToCoordinate.put(AlGEBREIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

	private static String[] initializeAlgebraicNotation() {
        return new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
    }
	
}
