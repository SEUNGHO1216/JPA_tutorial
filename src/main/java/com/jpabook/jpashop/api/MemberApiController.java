package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        //근데 dto를 따로 만들거나 바로 id를 반환해도 되지 않는가
        return new CreateMemberResponse(id);
    }
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        //api만들 때는 직접 entity로 받는게 아니라 지금처럼 dto로 받고 주는게 훨씬 낫다.
        //실무에서는 직접 엔티티를 웹으로 노출시키거나 파라미터로 받는것은 없다..유지보수도 힘들다
        /*dto로 받아서 활용하자(지금처럼) - 데이터 손상의 우려도 우려지만, 엔티티는 굉장히 변동이 잦고
        여러 클래스에서 갖다 쓰는데, 세부 스펙하나가 변하면 전체 api 스펙이 다 변해버릴 수도 있음
        근데 엔티티르 정보 교환하면 그 하나의 메소드 또는 클래스에 맞추기 위해 엔티티 수정을 해야하는데 그럼 다른 클래스에서 오류들이 터진다..*/
        Member member = new Member();
        member.setName(request.name);
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long memberId,
                                               @RequestBody @Valid UpdateMemberRequest request){

        Member member = memberService.findOne(memberId);
        memberService.updateMember(member, request.getName());

        //사실상 서비스단의 transactional 끝난 후 영속성도 종료되어서 새로 find 해줌(근데 select 쿼리는 따로 안나간다..)
        Member updateMember = memberService.findOne(memberId);
        return new UpdateMemberResponse(updateMember.getId(), updateMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest{

        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;

    }

    @Data
    public static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id=id;
        }
    }

}
