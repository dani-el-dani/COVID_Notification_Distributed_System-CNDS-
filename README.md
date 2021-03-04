# COVID Notification Distributed System CNDS

This is to demonstrate how object can communicate in distributed system using java’s remote method invocation and also the use of vector clocks to force casual ordering (we say message is received in causal order if all messages that causally precede it have also been received as well). vector clocks are logical clocks used to oreder events in destributed system because it's hard to synchronize physical clocks in the system.
This a distributed system which helps users notify other users that they are sick to help the other users take action if they have contacts with them. Users notify their peers using Java RMI. In the system users can join and leave (died or just leave if they want) at any time. CNDS deliver notifications in the same order they were sent to every users if they are causally related.


First go to the directory where the java files are in the command prompt and compile the files using javac command.
javac *.java

Then run rmic command to generate stubs.
rmic CNDSFirstParticipantImpl
rmic CNDSParticipantImpl

Then run the registry using this command.
rmiregistry

After the registry starts running run the first participant in another command prompt using.
java FirstParticipant

Finally run as many participants as you want in different command prompt using.
java Participant
