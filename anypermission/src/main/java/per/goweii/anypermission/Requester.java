package per.goweii.anypermission;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/16
 */
interface Requester<R> {
    R request(RequestListener listener);
}
