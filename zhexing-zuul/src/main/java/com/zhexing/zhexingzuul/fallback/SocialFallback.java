package com.zhexing.zhexingzuul.fallback;

import com.zhexing.common.pojo.Comment;
import com.zhexing.common.pojo.Dynamic;
import com.zhexing.common.pojo.Follow;
import com.zhexing.common.resultPojo.ZheXingResult;
import com.zhexing.zhexingzuul.service.SocialService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SocialFallback implements FallbackFactory<SocialService> {


    @Override
    public SocialService create(Throwable throwable) {

        ZheXingResult result = ZheXingResult.build(418, "服务器内部错误！！Feign回调错误！！！");

        return new SocialService() {
            @Override
            public ZheXingResult dynamicComment(Comment comment, String tnames) {
                return result;
            }

            @Override
            public ZheXingResult deleteComment(Long commentId, Long dynamicId, String tnames) {
                return result;
            }

            @Override
            public ZheXingResult searchHotComment(Long userId, Long dynamicId, int start, int n) {
                return result;
            }

            @Override
            public ZheXingResult likeComment(Long dynamicId, Long commentId, Long userId, String tname, boolean flag) {
                return result;
            }

            @Override
            public ZheXingResult getDynamicComments(Long dynamicId, int start, int n, Long userId) {
                return result;
            }

            @Override
            public ZheXingResult commentComment(Comment comment) {
                return result;
            }

            @Override
            public ZheXingResult deleteCComment(Long commentId, Long parentId) {
                return result;
            }

            @Override
            public ZheXingResult getCComments(Long parentId, Long userId) {
                return result;
            }

            @Override
            public ZheXingResult publishDynamic(Dynamic dynamic) {
                return result;
            }

            @Override
            public ZheXingResult deleteDynamic(Long dynamicId, String tnames) {
                return result;
            }

            @Override
            public ZheXingResult searchHotDynamic(String tname, int start, int nums, Long userId) {
                return result;
            }

            @Override
            public ZheXingResult recommendDynamic(Long start, Long end, Long userId) {
                return result;
            }

            @Override
            public ZheXingResult likeDynamic(Long userId, Long dynamicId, String tnames, boolean flag) {
                return result;
            }

            @Override
            public ZheXingResult allFollowDynamics(Long userId, Long start, Long end) {
                return result;
            }

            @Override
            public ZheXingResult followDynamic(Long userId, String uname, Long start, Long end) {
                return result;
            }

            @Override
            public ZheXingResult forwardDynamic(Dynamic dynamic) {
                return result;
            }

            @Override
            public ZheXingResult collectDynamic(Long dynamicId) {
                return result;
            }

            @Override
            public ZheXingResult uploadFile(MultipartFile uploadFile) {
                return result;
            }

            @Override
            public ZheXingResult uploadImage64(String image) {
                return result;
            }

            @Override
            public ZheXingResult followUser(Follow follow, boolean flag) {
                return result;
            }

            @Override
            public ZheXingResult searchAllFollow(Long userId) {
                return result;
            }

            @Override
            public ZheXingResult searchAllFollowedUser(Long follower) {
                return result;
            }

            @Override
            public ZheXingResult index(String uname, Long userId) {
                return result;
            }

            @Override
            public ZheXingResult getUser(Long userId, String user, Long start, Long end) {
                return result;
            }

            @Override
            public ZheXingResult recommendUser(Long userId) {
                return result;
            }

            @Override
            public ZheXingResult getIPPort() {
                return result;
            }

            @Override
            public ZheXingResult getMessage(Long userId) {
                return result;
            }

            @Override
            public ZheXingResult delMessage(String key) {
                return result;
            }

            @Override
            public ZheXingResult hotTag() {
                return result;
            }

            @Override
            public ZheXingResult searchTag(String tname) {
                return result;
            }
        };
    }
}
