Non-blocking MELSEC Communication protocol for Java.

Quick Start
--------
```java
MelsecClientConfig config = new MelsecClientConfig.Builder("localhost").build();
MelsecTcpClient client = MelsecTcpClient.create3EBinary(config);

client.batchRead("D100", 3)
    .thenAccept(response -> {
        System.out.println(response.readShort());
        System.out.println(response.readShort());
        System.out.println(response.readShort());
    });
```

See the examples project for more.

Supported format
-------
* QnA compatible 3E Ascii
* QnA compatible 3E Binary
* 4E Ascii (TODO)
* 4E Binary (TODO)

License
--------
Apache License, Version 2.0