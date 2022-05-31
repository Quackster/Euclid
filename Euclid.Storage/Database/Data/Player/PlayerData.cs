using FluentNHibernate.Mapping;
using System;

namespace Euclid.Storage.Database.Data
{
    public class PlayerDataMapping : ClassMap<PlayerData>
    {
        public PlayerDataMapping()
        {
            Table("user");
            Id(x => x.Id, "id").GeneratedBy.Identity();
            Map(x => x.Name, "username");
            Map(x => x.Password, "password");
            Map(x => x.Email, "email");
            Map(x => x.Figure, "figure");
            Map(x => x.DirectMail, "direct_mail");
            Map(x => x.Birthday, "birthday");
            Map(x => x.PhoneNumber, "phone_number");
            Map(x => x.CustomData, "custom_data");
            Map(x => x.Sex, "sex");
            Map(x => x.Country, "country");
            Map(x => x.JoinDate, "join_date").Generated.Insert();
            Map(x => x.LastOnline, "last_online").Generated.Insert();
        }
    }

    public class PlayerData : IEntityData
    {
        public virtual int Id { get; set; }
        public virtual string Name { get; set; }
        public virtual string Password { get; set; }
        public virtual string Email { get; set; }
        public virtual string Figure { get; set; }
        public virtual bool DirectMail { get; set; }
        public virtual string Birthday { get; set; }
        public virtual string PhoneNumber { get; set; }
        public virtual string CustomData { get; set; }
        public virtual string Sex { get; set; }
        public virtual string Country { get; set; }
        public virtual DateTime JoinDate { get; set; }
        public virtual DateTime LastOnline { get; set; }
        public virtual int Rank { get { return 1; } }
    }
}
