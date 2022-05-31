using Euclid.Game.Entity.Humanoid;
using Euclid.Messages.Outgoing;
using Euclid.Util.Extensions;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Game
{
    public abstract class RoomEntity
    {
        public Humanoid Entity { get; set; }
        public Room Room { get; set; }
        public Position Position { get; set; }
        public Position Next { get; set; }
        public Position Goal { get; set; }
        public List<Position> PathList { get; private set; }
        public bool IsWalking { get; set; }
        public int InstanceId { get; set; }
        public bool NeedsUpdate { get; set; }
        public int RoomId => Room != null ? Room.Data.Id : 0;
        public RoomTile CurrentTile => Position != null ? (Position.GetTile(Room) ?? null) : null;
        public Item CurrentItem => CurrentTile?.HighestItem;
        public RoomTimerManager TimerManager { get; set; }
        public string AuthenticateTeleporterId { get; set; }
        public int AuthenticateRoomId { get; set; }
        public bool WalkingAllowed { get; internal set; }
        public virtual ITaskObject TaskObject { get; set; }
        public int DanceId { get; set; }
        public bool IsDancing => DanceId > 0;
        public bool HasEffect => EffectId > 0;
        public bool IsSitting => Status.ContainsKey("sit");
        public int EffectId { get; set; }
        public bool BeingKicked { get; set; }

        /// <summary>
        /// Get the status handling, the value is the value string and the time it was added.
        /// </summary>
        public ConcurrentDictionary<string, RoomUserStatus> Status { get; set; }

        public RoomEntity(Humanoid entity)
        {
            Entity = entity;
            TimerManager = new RoomTimerManager();
            AuthenticateTeleporterId = null;
        }

        /// <summary>
        /// Reset handler called when user leaves room
        /// </summary>
        public void Reset()
        {
            Status = new ConcurrentDictionary<string, RoomUserStatus>();
            IsWalking = false;
            Goal = null;
            Next = null;
            InstanceId = -1;
            DanceId = 0;
            Room = null;
            TimerManager.Reset();
            WalkingAllowed = true;
        }

        /// <summary>
        /// Chat message handling
        /// </summary>
        public void Talk(ChatMessageType chatMessageType, string chatMsg, List<Player> recieveMessages = null)
        {
            if (recieveMessages == null)
                recieveMessages = Room.EntityManager.GetEntities<Player>();

            recieveMessages = recieveMessages.Distinct().ToList();

            // Send talk message to room
            foreach (Player player in recieveMessages)
            {
                switch (chatMessageType)
                {
                    case ChatMessageType.CHAT:
                        player.Send(new CHAT(this.Entity.EntityData.Name, chatMsg));
                        break;
                    case ChatMessageType.SHOUT:
                        player.Send(new SHOUT(this.Entity.EntityData.Name, chatMsg));
                        break;
                    case ChatMessageType.WHISPER:
                        player.Send(new WHISPER(this.Entity.EntityData.Name, chatMsg));
                        break;
                }
            }
        }

        /// <summary>
        /// Get chat gesture for message
        /// </summary>
        private int GetChatGesture(string chatMsg)
        {
            chatMsg = chatMsg.ToLower();

            if (chatMsg.Contains(":)") || chatMsg.Contains(":d") || chatMsg.Contains("=]") ||
                chatMsg.Contains("=d") || chatMsg.Contains(":>"))
            {
                return 1;
            }

            if (chatMsg.Contains(">:(") || chatMsg.Contains(":@"))
                return 2;

            if (chatMsg.Contains(":o"))
                return 3;

            if (chatMsg.Contains(":(") || chatMsg.Contains("=[") || chatMsg.Contains(":'(") || chatMsg.Contains("='["))
                return 4;

            return 0;
        }

        /// <summary>
        /// Request move handler
        /// </summary>
        /// <param name="x">x coord goal</param>
        /// <param name="y">y coord goal</param>
        public void Move(int x, int y)
        {
            if (Room == null)
                return;

            if (Next != null)
            {
                var oldPosition = Next.Copy();
                Position.X = oldPosition.X;
                Position.Y = oldPosition.Y;
                Position.Z = Room.Model.TileHeights[oldPosition.X, oldPosition.Y];
                NeedsUpdate = true;
            }

            Goal = new Position(x, y);

            var goalTile = Goal.GetTile(Room);

            if (!RoomTile.IsValidTile(Room, Entity, Goal))
                return;

            var pathList = Pathfinder.FindPath(Entity, Room, Position, Goal);

            if (pathList == null || !pathList.Any())
                return;

            PathList = pathList;
            IsWalking = true;
        }

        /// <summary>
        /// Kick a user from the room.
        /// </summary>

        public void kick(bool allowWalking = true)
        {
            this.BeingKicked = true;
            Position doorLocation = this.Room.Model.Door;


            // If we're standing in the door, walk to room
            if (this.Position == doorLocation)
            {
                this.Room.EntityManager.LeaveRoom(this.Entity);
                return;
            }

            // Attempt to walk to the door
            this.Move(doorLocation.X, doorLocation.Y);
            this.WalkingAllowed = allowWalking;

            // If user isn't walking, leave immediately
            if (!this.IsWalking)
            {
                this.Room.EntityManager.LeaveRoom(this.Entity);
            }
        }

        /// <summary>
        /// Stopped walking handler
        /// </summary>
        public void StopWalking()
        {
            if (!this.IsWalking)
                return;

            this.IsWalking = false;
            this.PathList.Clear();
            this.Next = null;
            this.RemoveStatus("mv");
            this.InteractItem();
            this.NeedsUpdate = true;

            bool leaveRoom = this.BeingKicked;
            Position doorPosition = this.Room.Model.Door;//.getModel().getDoorLocation();

            if (doorPosition == this.Position)
            {
                leaveRoom = true;
            }

            if (this.Room.Data.IsPublicRoom)
            {
                /*if (WalkwaysManager.getInstance().getWalkway(this.getRoom(), doorPosition) != null)
                {
                    leaveRoom = false;
                }*/
            }

            // Leave room if the tile is the door and we are in a flat or we're being kicked
            if (leaveRoom || this.BeingKicked)
            {
                this.Room.EntityManager.LeaveRoom(this.Entity, true);
                return;
            }
        }

        /// <summary>
        /// Interact with current item by calling entity stop on interactor
        /// </summary>
        public void InteractItem()
        {
            var roomTile = CurrentTile;

            if (roomTile == null)
                return;

            Position.Z = roomTile.GetWalkingHeight();

            Item item = CurrentItem;

            if (item == null || (
                !item.Definition.Data.IsChair ||
                !item.Definition.Data.IsBed))
            {
                if (ContainsStatus("sit") || ContainsStatus("lay"))
                {
                    RemoveStatus("sit");
                    RemoveStatus("lay");
                }
            }

            if (item != null && Entity is Humanoid humanoid)
            {
                if (item.Definition.Data.IsChair)
                {
                    humanoid.RoomEntity.Position.Rotation = item.Position.Rotation;
                    humanoid.RoomEntity.AddStatus("sit", Convert.ToString((int)item.Height));
                    humanoid.RoomEntity.NeedsUpdate = true;
                }

                if (item.Definition.Data.IsBed)
                {
                    humanoid.RoomEntity.Position.Rotation = item.Position.Rotation;
                    humanoid.RoomEntity.AddStatus("lay", Convert.ToString((int)item.Height));
                    humanoid.RoomEntity.NeedsUpdate = true;
                }
                //item.Interactor.OnStop(this.Entity);
            }

            this.NeedsUpdate = true;
        }

        /// <summary>
        /// Get if entity contains status
        /// </summary>
        public bool ContainsStatus(string statusKey)
        {
            return Status.ContainsKey(statusKey);
        }

        /// <summary>
        /// Adds a status with a key and value, along with the int64 time of when the status was added.
        /// </summary>
        /// <param name="key">the key</param>
        /// <param name="value">the value</param>
        public void AddStatus(string key, string value)
        {
            this.RemoveStatus(key);
            Status.TryAdd(key, new RoomUserStatus(key, value));
        }

        /// <summary>
        /// Removes a status by its given key
        /// </summary>
        /// <param name="key">the key to check for</param>
        public void RemoveStatus(string key)
        {
            if (Status.ContainsKey(key))
                this.Status.Remove(key);
        }

        /// <summary>
        /// Warp etntiy to position
        /// </summary>
        /// <param name="id"></param>
        /// <param name="v"></param>
        public void Warp(Position targetPosition, bool instantUpdate = false)
        {
            RoomTile oldTile = CurrentTile;

            if (oldTile != null)
            {
                oldTile.RemoveEntity(Entity);
            }

            if (Next != null)
            {
                RoomTile nextTile = Next.GetTile(Room);

                if (nextTile != null)
                {
                    nextTile.RemoveEntity(Entity);
                }
            }

            Position = targetPosition.Copy();
            RefreshHeight(targetPosition);

            RoomTile newTile = CurrentTile;

            if (newTile != null)
            {
                newTile.AddEntity(Entity);
            }

            if (instantUpdate && Room != null)
            {
                InteractItem();
                //Room.Send(new UsersStatusComposer(List.Create(Entity)));
            }
        }

        /// <summary>
        /// Refresh height of entity
        /// </summary>
        private void RefreshHeight(Position newPosition)
        {
            var targetPosition = newPosition ?? Position;

            var oldTile = Position.GetTile(Room);
            var newTile = targetPosition.GetTile(Room);

            if (oldTile.GetWalkingHeight() != newTile.GetWalkingHeight())
            {
                Position.Z = newTile.GetWalkingHeight();
                NeedsUpdate = true;
            }
        }
    }

}