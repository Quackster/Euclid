using Euclid.Game.Entity.Humanoid;
using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class Pathfinder
    {
        private static bool NoDiag = false;
        private static double MAX_DROP_HEIGHT = 3.0;
        private static double MAX_LIFT_HEIGHT = 1.5;

        public static List<Position> FindPath(Humanoid entity, Room room, Position start, Position end)
        {
            List<Position> Path = new List<Position>();

            PathfinderNode Nodes = FindPathReversed(entity, room, end, start);

            if (Nodes != null) // make sure we do have a path first
            {
                while (Nodes.Next != null)
                {
                    Path.Add(Nodes.Next.Position);
                    Nodes = Nodes.Next;
                }
            }

            // I need to change 'IsValidStep' to not count the position the user is on (the user who wants to walk) or the emulator will error..

            return Path;
        }

        private static PathfinderNode FindPathReversed(Humanoid entity, Room room, Position start, Position end)
        {
            MinHeap<PathfinderNode> openList = new MinHeap<PathfinderNode>(256);

            PathfinderNode[,] map = new PathfinderNode[room.Model.MapSizeX, room.Model.MapSizeY];
            PathfinderNode node;
            Position tmp;
            int Cost;
            int Diff;

            PathfinderNode current = new PathfinderNode(start);
            current.Cost = 0;

            PathfinderNode Finish = new PathfinderNode(end);
            map[current.Position.X, current.Position.Y] = current;
            openList.Add(current);

            while (openList.Count > 0)
            {
                current = openList.ExtractFirst();
                current.InClosed = true;

                for (int i = 0; NoDiag ? i < NoDiagMovePoints.Length : i < MovePoints.Length; i++)
                {
                    tmp = current.Position + (NoDiag ? NoDiagMovePoints[i] : MovePoints[i]);
                    bool isFinalMove = (tmp.X == end.X && tmp.Y == end.Y); // are we at the final position?

                    if (IsValidStep(room, entity, new Position(current.Position.X, current.Position.Y), tmp, isFinalMove)) // need to set the from positions
                    {
                        if (map[tmp.X, tmp.Y] == null)
                        {
                            node = new PathfinderNode(tmp);
                            map[tmp.X, tmp.Y] = node;
                        }
                        else
                        {
                            node = map[tmp.X, tmp.Y];
                        }

                        if (!node.InClosed)
                        {
                            Diff = 0;

                            if (current.Position.X != node.Position.X)
                            {
                                Diff += 2;
                            }

                            if (current.Position.Y != node.Position.Y)
                            {
                                Diff += 2;
                            }

                            Cost = current.Cost + Diff + node.Position.GetDistanceSquared(end);

                            if (Cost < node.Cost)
                            {
                                node.Cost = Cost;
                                node.Next = current;
                            }

                            if (!node.InOpen)
                            {
                                if (node.Equals(Finish))
                                {
                                    node.Next = current;
                                    return node;
                                }

                                node.InOpen = true;
                                openList.Add(node);
                            }
                        }
                    }
                }
            }

            return null;
        }

        /// <summary>
        /// Check if the step is valid
        /// </summary>
        public static bool IsValidStep(Room room, Humanoid entity, Position from, Position to, bool isFinalMove)
        {
            if (!RoomTile.IsValidTile(room, entity, to))
                return false;

            if (!RoomTile.IsValidTile(room, entity, from))
              return false;


            var fromTile = from.GetTile(room);
            var toTile = to.GetTile(room);

            double oldHeight = fromTile.WalkingHeight;
            double newHeight = toTile.WalkingHeight;

            if (toTile.isHeightUpwards(fromTile))
            {
                if (Math.Abs(newHeight - oldHeight) > MAX_LIFT_HEIGHT)
                {
                    return false;
                }
            }

            if (toTile.isHeightDrop(fromTile))
            {
                if (Math.Abs(oldHeight - newHeight) > MAX_DROP_HEIGHT)
                {
                    return false;
                }
            }

            if (fromTile.isHeightUpwards(toTile))
            {
                if (Math.Abs(newHeight - oldHeight) > MAX_LIFT_HEIGHT)
                {
                    return false;
                }
            }

            if (fromTile.isHeightDrop(toTile))
            {
                if (Math.Abs(oldHeight - newHeight) > MAX_DROP_HEIGHT)
                {
                    return false;
                }
            }

            /*
        double oldHeight = fromTile.getWalkingHeight();
        double newHeight = toTile.getWalkingHeight();

        Item fromItem = fromTile.getHighestItem();
        Item toItem = toTile.getHighestItem();

        boolean hasPool = room.getModel().getName().startsWith("pool_") || room.getModel().getName().equals("md_a");
        boolean isPrivateRoom =  !room.isPublicRoom();

        if (hasPool || isPrivateRoom) {
            if (hasPool) {
                if (oldHeight - 3 >= newHeight) {
                    return fromItem != null && (fromItem.hasBehaviour(ItemBehaviour.TELEPORTER)
                            || (fromItem.getDefinition().getSprite().equals("wsJoinQueue"))
                            || (fromItem.getDefinition().getSprite().equals("wsQueueTile"))
                            || (fromItem.getDefinition().getSprite().equals("poolEnter"))
                            || (fromItem.getDefinition().getSprite().equals("poolExit")));
                }

            }

            if (oldHeight + 1.5 <= newHeight) {
                return toItem != null && (toItem.hasBehaviour(ItemBehaviour.TELEPORTER)
                        || (toItem.getDefinition().getSprite().equals("wsJoinQueue"))
                        || (toItem.getDefinition().getSprite().equals("poolEnter"))
                        || (toItem.getDefinition().getSprite().equals("poolExit")));
            }
        } else {
            // Apply this to the rest of public rooms
            if (oldHeight - 1.5 >= newHeight) {
                return false;
            }

            if (oldHeight + 1.5 <= newHeight) {
                return false;
            }
        }*/

            return true;
        }

        public static Position[] MovePoints = new Position[]
        {
            new Position(-1, -1),
            new Position(0, -1),
            new Position(1, -1),
            new Position(1, 0),
            new Position(1, 1),
            new Position(0, 1),
            new Position(-1, 1),
            new Position(-1, 0)
        };

        private static Position[] NoDiagMovePoints = new Position[]
        {
            new Position(0, -1),
            new Position(1, 0),
            new Position(0, 1),
            new Position(-1, 0)
        };
    }
}
