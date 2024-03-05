# Euclid

A private Habbo Hotel server designed for emulating the ~2001 version of Habbo written in C# using .NET Core.

Euclid uses DotNetty for asynchronous TCP sockets and networking. Fluid NHibernate for quick and easy database access and SQL queries on the fly without having to manually write queries and Newtonsoft.Json for JSON serializing and deserializing for various custom item attributes.

## Download

The latest builds for Linux and Windows are found on the [latest](https://github.com/Quackster/Euclid/releases/tag/latest) tag. This project requires .NET 8.

| OS | Download |
|---|---|
| Linux (64-bit) | [Euclid-linux-x64.zip](https://github.com/Quackster/Euclid/releases/download/latest/Euclid-linux-x64.zip) |
| Windows (64-bit) | [Euclid-win-x64.zip](https://github.com/Quackster/Euclid/releases/download/latest/Euclid-win-x64.zip) |

### Cloning this repository

```
$ git clone --recursive https://github.com/Quackster/Euclid
```

**or**

```
$ git clone https://github.com/Quackster/Euclid
$ git submodule update --init --recursive
```

# Images

![](https://i.imgur.com/yciWRix.png)

![](https://i.imgur.com/cQDEDyf.png)

![](https://i.imgur.com/hMA8Ypf.png)
