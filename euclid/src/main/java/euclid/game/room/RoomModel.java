package euclid.game.room;

import euclid.game.pathfinder.Position;
import euclid.storage.database.data.RoomModelData;

public class RoomModel {

    private int mapSizeX;
    private int mapSizeY;
    private TileState[][] tileStates;
    private double[][] tileHeights;
    private String heightmap;
    private RoomModelData data;

    public RoomModel(RoomModelData data) {
        this.data = data;
        parse();
    }

    private void parse() {
        String[] lines = data.getHeightmap().split("\\|");

        mapSizeY = lines.length;
        mapSizeX = lines[0].length();

        tileStates = new TileState[mapSizeX][mapSizeY];
        tileHeights = new double[mapSizeX][mapSizeY];

        StringBuilder heightmapBuilder = new StringBuilder();

        for (int y = 0; y < mapSizeY; y++) {
            String line = lines[y];

            for (int x = 0; x < mapSizeX; x++) {
                try {
                    String tile = String.valueOf(line.charAt(x));

                    if (isNumeric(tile)) {
                        tileStates[x][y] = TileState.OPEN;
                        tileHeights[x][y] = Double.parseDouble(tile);
                    } else {
                        tileStates[x][y] = TileState.CLOSED;
                        tileHeights[x][y] = 0;
                    }

                    if (data.getDoorX() == x && data.getDoorY() == y) {
                        tileStates[x][y] = TileState.OPEN;
                        tileHeights[x][y] = data.getDoorZ();
                        heightmapBuilder.append(data.getDoorZ());
                    } else {
                        heightmapBuilder.append(tile);
                    }
                } catch (Exception ignored) {
                }
            }

            heightmapBuilder.append("\r");
        }

        heightmap = heightmapBuilder.toString();
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty())
            return false;
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public TileState[][] getTileStates() {
        return tileStates;
    }

    public double[][] getTileHeights() {
        return tileHeights;
    }

    public String getHeightmap() {
        return heightmap;
    }

    public RoomModelData getData() {
        return data;
    }

    public void setData(RoomModelData data) {
        this.data = data;
    }

    public Position getDoor() {
        return new Position(data.getDoorX(), data.getDoorY(), data.getDoorZ(), data.getDoorDirection(), data.getDoorDirection());
    }

    public boolean isTile(Position position) {
        if (position.getX() >= 0 && position.getY() >= 0 && position.getX() < this.mapSizeX && position.getY() < this.mapSizeY) {
            return true;
        }
        return false;
    }
}
