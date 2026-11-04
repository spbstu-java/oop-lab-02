public class Lab2 {
  public static void main(String[] args) {
    RepeatableService service = new RepeatableService();
    service.get("get_method_param");
    service.add(1, 2);
    service.printProtected("print_param", 1);
    service.inspectProtected("inspect_param", RepeatableService.Status.READY, true);
    service.runPrivateWork();
  }
}
