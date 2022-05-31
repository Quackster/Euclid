﻿using Euclid.Game.Entity.Humanoid;
using log4net;
using System;
using System.Timers;

namespace Euclid.Game
{
    public class StatusTask : IRoomTask
    {
        private static readonly ILog log = LogManager.GetLogger(typeof(StatusTask));
        private Room room;

        /// <summary>
        /// Set task interval, which is 1000ms for user maintenance
        /// </summary>
        public override int Interval => 1000;

        /// <summary>
        /// Constructor for the entity task
        /// </summary>
        public StatusTask(Room room)
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
                // foreach (IEntity entity in room.Entities.Values)
                {
                    /*
                    if (entity.RoomEntity.RoomId != room.Data.Id)
                        continue;

                    ProcessEntity(entity);*/
                }
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
        private void ProcessEntity(Humanoid entity)
        {

        }
    }
}
