﻿using System;
using System.Linq;
using System.Collections.Generic;
using Euclid.Game.Entity.Humanoid;

namespace Euclid.Game
{
    public class Position
    {
        public int X;
        public int Y;
        public double Z;
        public int BodyRotation;
        public int HeadRotation;

        public int Rotation
        {
            get
            {
                return BodyRotation;
            }
            set
            {
                BodyRotation = value;
                HeadRotation = value;
            }
        }

        public Position() : this(0, 0, 0)
        {
        }

        public Position(int x, int y)
        {
            X = x;
            Y = y;
            Z = 0;
        }

        public Position(int x, int y, double z)
        {
            X = x;
            Y = y;
            Z = z;
        }

        public Position(int x, int y, double z, int headRotation, int bodyRotation)
        {
            X = x;
            Y = y;
            Z = z;
            HeadRotation = headRotation;
            BodyRotation = bodyRotation;
        }

        /// <summary>
        /// Get current tile by supplying room instance
        /// </summary>
        public RoomTile GetTile(Room room)
        {
            if (room == null || room.Model == null || !room.Model.IsTile(this))
                return null;

            if (room.Model.TileStates[this.X, this.Y] == TileState.CLOSED)
                return null;


            return room.Mapping.Tiles[X, Y];
        }

        /// <summary>
        /// Get if the position is adjacent to this position
        /// </summary>
        public bool Touches(Position position)
        {
            return GetDistanceSquared(position) <= 2;
        }


        /// <summary>
        /// Add coordinates to current position
        /// </summary>
        public Position Add(Position other)
        {
            return new Position(other.X + X, other.Y + Y, other.Z + Z);
        }

        /// <summary>
        /// Subtract coordinates to current position
        /// </summary>
        public Position Subtract(Position other)
        {
            return new Position(other.X - X, other.Y - Y, other.Z - Z);
        }

        /// <summary>
        /// Get the closest tile to this current tile
        /// </summary>
        public Position ClosestTile(Room room, Position other, Humanoid entity = null)
        {
            List<Position> potentialTiles = new List<Position>();

            for (int i = 0; i < Pathfinder.MovePoints.Length; i++)
                potentialTiles.Add(Copy() + Pathfinder.MovePoints[i]);


            potentialTiles.RemoveAll(x => !RoomTile.IsValidTile(room, entity, x));

            if (potentialTiles.Count > 0)
            {
                potentialTiles = potentialTiles.OrderBy(x => x.GetDistanceSquared(other)).ToList();
                return potentialTiles[0];
            }

            return null;
        }

        /// <summary>
        /// Get distance between two coordinates
        /// </summary>
        public int GetDistanceSquared(Position point)
        {
            int dx = X - point.X;
            int dy = Y - point.Y;

            return (int)Math.Sqrt((dx * dx) + (dy * dy));
        }

        public Position GetSquareInFront()
        {
            Position square = Copy();

            if (BodyRotation == 0)
            {
                square.Y--;
            }
            else if (BodyRotation == 1)
            {
                square.X++;
                square.Y--;
            }
            else if (BodyRotation == 2)
            {
                square.X++;
            }
            else if (BodyRotation == 3)
            {
                square.X++;
                square.Y++;
            }
            else if (BodyRotation == 4)
            {
                square.Y++;
            }
            else if (BodyRotation == 5)
            {
                square.X--;
                square.Y++;
            }
            else if (BodyRotation == 6)
            {
                square.X--;
            }
            else if (BodyRotation == 7)
            {
                square.X--;
                square.Y--;
            }

            return square;
        }


        public Position GetSquareBehind()
        {
            Position square = Copy();

            if (BodyRotation == 0)
            {
                square.Y++;
            }
            else if (BodyRotation == 1)
            {
                square.X--;
                square.Y++;
            }
            else if (BodyRotation == 2)
            {
                square.X--;
            }
            else if (BodyRotation == 3)
            {
                square.X--;
                square.Y--;
            }
            else if (BodyRotation == 4)
            {
                square.Y--;
            }
            else if (BodyRotation == 5)
            {
                square.X++;
                square.Y--;
            }
            else if (BodyRotation == 6)
            {
                square.X++;
            }
            else if (BodyRotation == 7)
            {
                square.X++;
                square.Y++;
            }

            return square;
        }

        public Position GetSquareRight()
        {
            Position square = Copy();

            if (BodyRotation == 0)
            {
                square.X++;
            }
            else if (BodyRotation == 1)
            {
                square.X++;
                square.Y++;
            }
            else if (BodyRotation == 2)
            {
                square.Y++;
            }
            else if (BodyRotation == 3)
            {
                square.X--;
                square.Y++;
            }
            else if (BodyRotation == 4)
            {
                square.X--;
            }
            else if (BodyRotation == 5)
            {
                square.X--;
                square.Y--;
            }
            else if (BodyRotation == 6)
            {
                square.Y--;
            }
            else if (BodyRotation == 7)
            {
                square.X++;
                square.Y--;
            }

            return square;
        }

        public Position GetSquareLeft()
        {
            Position square = Copy();

            if (BodyRotation == 0)
            {
                square.X--;
            }
            else if (BodyRotation == 1)
            {
                square.X--;
                square.Y--;
            }
            else if (BodyRotation == 2)
            {
                square.Y--;
            }
            else if (BodyRotation == 3)
            {
                square.X++;
                square.Y--;
            }
            else if (BodyRotation == 4)
            {
                square.X++;
            }
            else if (BodyRotation == 5)
            {
                square.X++;
                square.Y++;
            }
            else if (BodyRotation == 6)
            {
                square.Y++;
            }
            else if (BodyRotation == 7)
            {
                square.X--;
                square.Y++;
            }

            return square;
        }

        public static bool operator ==(Position one, Position two)
        {
            if (one is null && two is null)
                return true;

            if (one is Position && two is Position)
                return one.Equals(two);

            return false;
        }

        public static bool operator !=(Position one, Position two)
        {
            if (one is null && !(two is null))
                return true;

            if (!(one is null) && two is null)
                return true;

            if (one is Position && two is Position)
                return !one.Equals(two);

            return false;
        }

        public static Position operator +(Position one, Position two)
        {
            return one.Copy().Add(two);
        }

        public static Position operator -(Position one, Position two)
        {
            return new Position(one.X - two.X, one.Y - two.Y);
        }

        public Position Copy()
        {
            return new Position(X, Y, Z, HeadRotation, BodyRotation);
        }

        public override bool Equals(object obj)
        {
            if (obj == null)
                return false;

            var t = obj as Position;

            if (t == null)
                return false;

            if (X == t.X && Y == t.Y)
                return true;

            return false;
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }

        public override string ToString()
        {
            return $"[X: {X}, Y: {Y}, Z: {Z}, HeadRotation: {HeadRotation}, BodyRotation: {BodyRotation}]";
        }
    }
}
