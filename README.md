To run the test:

Set version in plugins.sbt to 0.11.14
```
cd scala
sbt "project root; package"
```

This would result in the error 
```
name clash: clearExtension(com.google.protobuf.GeneratedMessage.GeneratedExtension<com.google.protobuf.DescriptorProtos.ServiceOptions,?>) in com.google.protobuf.DescriptorProtos.ServiceOptions.Builder and clearExtension(com.google.protobuf.GeneratedMessage.GeneratedExtension<com.google.protobuf.DescriptorProtos.ServiceOptions,T>) in com.google.protobuf.GeneratedMessageV3.ExtendableBuilder have the same erasure, yet neither overrides the other
[error] @java.lang.Override
[error]       public <Type> Builder clearExtension(
[error]           com.google.protobuf.GeneratedMessage.GeneratedExtension<
[error]               com.google.protobuf.DescriptorProtos.ServiceOptions, ?> extension) {
[error]         return super.clearExtension(extension);
[error]       }
```
Changing the version back to 0.11.13 would result in a success build