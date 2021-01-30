# Skonnection

## Présentation

➠  Type : [Skript](https://github.com/SkriptLang/Skript)'s Addon \
➠  Développer(s) : [All contributors](https://github.com/BakaAless/Skonnection/graphs/contributors)


## Description

➠  This plugin is a [Skript](https://github.com/SkriptLang/Skript)'s addon to use java Socket, create ServerSocket and
use Minecraft's plugin messages.

* Conditions :
  * Sockets:
    * `%socket% [('s| is)] connect[ed]`
  * Plugin Messages:
    * `%string% [('s| is)] valid [channel]`
  

* Effets :
  * Sockets :
    * `send data %string% from socket %socket%`
    * `disconnect socket %socket%`
    * `destroy socket %socket%`
    * `send data %string% from server[ ][socket] %serversocket% [(to %clientsocket%)]`
    * `disconnect server[ ][socket] %serversocket%`
    * `destroy server[ ][socket] %serversocket%`
  * Plugin Messages:
    * `register channel %string%`
    * `send plugin[ ]message %pluginmessage% through [channel] %string% [(with %player%)]`  
    * `unregister channel %string%`


* Expressions :
  * Sockets:
    * `create [client ]socket [to] %string%`
    * `create server[ ][socket] [with port] %integer%`
    * `[get] uuid of socket %socket%` or `[get] socket %socket%'s uuid`
    * `[get] uuid of server[ ][socket] %serversocket%` or `[get] server[ ][socket] %serversocket%'s uuid`
    * `[get] uuid of client %clientsocket%` or `[get] client %clientsocket%'s uuid`
    * `[get] ip of socket %socket%` or `[get] socket %socket%'s ip`
    * `[get] port of server[ ][socket] %serversocket%` or `[get] server[ ][socket] %serversocket%'s port`
    * `[get] ip of client %clientsocket%` or `[get] client %clientsocket%'s ip`
    * `[get] client[s] of server[ ][socket] %serversocket%` or `[get] server[ ][socket] %serversocket%'s client[s]`
    * `[get] server of client %clientsocket%` or `[get] client %clientsocket%'s server`
  * Plugin Messages:
    * `create pluginmessage`
    * `retrieve %data%`
    * `data of %pluginmessage%` (can be modified with add and remove)


* Events :
  * Sockets:
    * `socket receive data [async]` :
      * socket : `event-socket`
      * data : `event-text`
    * `[client] attempt to connect [on server][async]` :
      * server : `event-serversocket`
      * client : `event-clientsocket`
    * `server[ ][socket] receive data [async]` :
      * server : `event-serversocket`
      * client : `event-clientsocket`
      * data : `event-text`
    * `socket disconnected` :
      * socket : `event-socket`
    * `client disconnected` :
      * client : `event-client`
  * Plugin Messages:
    * `receive plugin[ ]message` :
      * channel : `event-text`
      * data : `event-data`
      * player : `event-player`


* Types :
  * Sockets :
    * `socket`
    * `serversocket`
    * `clientsocket`
  * Plugin Messages:
    * `pluginmessage`
    * `data`


### Warning

➠ Socket's types can't be saved due to socket connection. Indeed, after a restart, the variables which
contains types are empty \
➠ If you want to send a plugin message when a player join the server (and if there is no other player)
you have to wait 10 ticks (average) to send the plugin message. Otherwise, the plugin message couldn't
be sent. \
➠ A channel have a specific pattern : `my:channel`. There is just an exception for BungeeCord's channel
which uses the old format : `BungeeCord`.

## License

➠  This program is under [GNU General Public License v3.0](https://github.com/BakaAless/SkSocket/blob/master/LICENSE).
