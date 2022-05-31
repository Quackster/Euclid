﻿using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Timers;
using Euclid.Messages.Outgoing;
using Euclid.Game.Entity.Humanoid;

namespace Euclid.Game
{
    public class EntityTask : IRoomTask
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(EntityTask));
        private Room room;

        /// <summary>
        /// Set task interval, which is 500ms for walking
        /// </summary>
        public override int Interval => 500;

        /// <summary>
        /// Constructor for the entity task
        /// </summary>
        public EntityTask(Room room)
        {
            this.room = room;
        }

        /// <summary>
        /// Run method called every 500ms
        /// </summary>
        /// <param name="state">whatever this means??</param>
        public override void Run(object sender, ElapsedEventArgs e)
        {
            try
            {
                var entityUpdates = new List<Humanoid>();

                foreach (Humanoid entity in room.EntityManager.GetEntities<Humanoid>())
                {
                    if (entity.RoomEntity.RoomId != room.Data.Id)
                        continue;

                    ProcessUser(entity);

                    if (entity.RoomEntity.NeedsUpdate)
                    {
                        entity.RoomEntity.NeedsUpdate = false;
                        entityUpdates.Add(entity);
                    }
                }

                
                 if (entityUpdates.Count > 0)
                     room.Send(new STATUS(entityUpdates));
                
            }
            catch (Exception ex)
            {
                log.Error(ex);
            }
        }

        /// <summary>
        /// Process user inside room
        /// </summary>
        /// <param name="entity">the entity to process</param>
        private void ProcessUser(Humanoid entity)
        {
            Position position = entity.RoomEntity.Position;
            Position goal = entity.RoomEntity.Goal;

            if (entity.RoomEntity.IsWalking)
            {
                if (entity.RoomEntity.Next != null)
                {
                    entity.RoomEntity.Position.X = entity.RoomEntity.Next.X;
                    entity.RoomEntity.Position.Y = entity.RoomEntity.Next.Y;
                    entity.RoomEntity.Position.Z = entity.RoomEntity.Position.GetTile(room).GetWalkingHeight();
                }

                if (entity.RoomEntity.PathList.Count > 0)
                {
                    Position next = entity.RoomEntity.PathList[0];
                    entity.RoomEntity.PathList.Remove(next);

                    var previousTile = entity.RoomEntity.CurrentTile;

                    if (previousTile != null)
                        previousTile.RemoveEntity(entity);

                    RoomTile nextTile = next.GetTile(room);

                    if (nextTile == null || !RoomTile.IsValidTile(room, entity, next, 
                        lastStep: !entity.RoomEntity.PathList.Any()))
                    {
                        entity.RoomEntity.PathList.Clear();
                        ProcessUser(entity);
                        entity.RoomEntity.Move(goal.X, goal.Y);
                        return;
                    }

                    nextTile.AddEntity(entity);

                    int rotation = Rotation.CalculateDirection(position.X, position.Y, next.X, next.Y);
                    double height = next.GetTile(room).GetWalkingHeight();

                    entity.RoomEntity.RemoveStatus("mv");
                    entity.RoomEntity.RemoveStatus("sit");
                    entity.RoomEntity.RemoveStatus("lay");

                    entity.RoomEntity.Position.Rotation = rotation;
                    entity.RoomEntity.AddStatus("mv", string.Format("{0},{1},{2}", next.X, next.Y, (int)height));
                    entity.RoomEntity.Next = next;
                }
                else
                {
                    entity.RoomEntity.StopWalking();
                }

                entity.RoomEntity.NeedsUpdate = true;
            }
        }
    }
}
