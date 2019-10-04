# Event Registration

1. To import the project, clone the repository above in a given directory with the command `git clone https://github.com/CodeWinger/ecse429-tutorial3-event-registration.git` 

2. In Eclipse, import the project named `EventRegistration-Spring` as a gradle project (File > Import... > (Select Gradle Folder) *Existing Gradle Project* > *Next* > Select root folder for project `EventRegistration-Spring` that you just cloned.

3. Click on *Finish*. 

4 (*Optional*) If you do not have the option in Eclipse to import the code as a gradle project, it may be that your Eclipse installation does not have the plugin installed. If that is the case, go to *Help* > *Eclipse MarketPlace*. In the search bar, type `gradle` and select `Buildship Gradle Integration 3.0` to install. Once you restart your eclipse, you should be able to import the Gradle project.   

5. Navigate to the class `TestEventRegistrationService` in package `ca.mcgill.ecse321.eventregistration.service`. Try to run the tests by clicking on *Run as* > *Junit*. The tests should all fail, this is normal.

6. In the [Test-driven software development lifecycle](https://technologyconversations.com/2013/12/20/test-driven-development-tdd-example-walkthrough/), the goal is to first write the tests before adding any new code. In this example, the method bodies for the methods of `createParticipant`, `createEvent`, `register` are purposefully left blank. By reading the test case, it should give you an idea as to what behavious is expected. 

If you have any questions, either Aren or me are available to answer them!
