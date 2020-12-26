# SkSocket

## Présentation

➠  Type : Skript's Addon \
➠  Développer(s) : [All contributors](https://github.com/BakaAless/SkSocket/graphs/contributors)


## Description

➠  This plugin is a Skript's addon to use java sockets and create ServerSockets.

* Conditions :
  * `%socket%[ is|'s] connect[ed]`


* Effets :
  * `send data %string% from socket %socket%`
  * `disconnect socket %socket%`
  * `destroy socket %socket%`
  * `send data %string% from server[ ][socket] %serversocket% [to %clientsocket%]`
  * `disconnect server[ ][socket] %serversocket%`
  * `destroy server[ ][socket] %serversocket%`
  

* Expressions :
  * `create [client ]socket [to ]%string%`
  * `create server[ ][socket] [with port ]%integer%`
  * `[get ]uuid of socket %socket%` or `[get ]socket %socket%'s uuid`
  * `[get ]uuid of server[ ][socket] %serversocket%` or `[get ]server[ ][socket] %serversocket%'s uuid`
  * `[get ]uuid of client %clientsocket%` or `[get ]client %clientsocket%'s uuid`
  * `[get ]ip of socket %socket%` or `[get ]socket %socket%'s ip`
  * `[get ]port of server[ ][socket] %serversocket%` or `[get ]server[ ][socket] %serversocket%'s port`
  * `[get ]ip of client %clientsocket%` or `[get ]client %clientsocket%'s ip`
  * `[get ]client[s] of server[ ][socket] %serversocket%` or `[get ]server[ ][socket] %serversocket%'s client[s]`
  * `[get ]server of client %clientsocket%` or `[get ]client %clientsocket%'s server`


* Events :
  * `socket receive data[ async]` :
    * socket : `event-socket`
    * data : `event-text`
  * `[client ]attempt to connect[ on server][async]` :
    * server : `event-serversocket`
    * client : `event-clientsocket`
  * `server[ ][socket] receive data[ async]` :
    * server : `event-serversocket`
    * client : `event-clientsocket`
    * data : `event-text`
  * `socket disconnected` :
    * socket : `event-socket`
  * `client disconnected` :
    * client : `event-client`
  

* Types :
  * `socket`
  * `serversocket`
  * `clientsocket`
  
### Warning

➠ Types can't be saved due to socket connection. Indeed, after a restart, the variables which contains types are empty

## License

➠  This program is under [GNU General Public License v3.0](https://github.com/BakaAless/SkSocket/blob/master/LICENSE).