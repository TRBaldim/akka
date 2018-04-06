package akka.cluster

object LoadBalancerApp extends App {

  BackendRouter.initiate(2551)
  BackendRouter.initiate(2552)
  BackendRouter.initiate(2561)

  FrontendRouter.initiate()

}
