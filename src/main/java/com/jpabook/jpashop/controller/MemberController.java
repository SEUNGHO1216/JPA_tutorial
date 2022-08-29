package com.jpabook.jpashop.controller;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.dto.MemberForm;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String join(Model model){
        model.addAttribute("memberForm", new MemberForm()); // 화면에서 객체에 접근이 가능하게함. validation을 가능하게 해줌
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String joinSubmit(@Valid MemberForm memberForm, BindingResult result){
        //@Valid 어노테이션을 통해 dto에서 지정한 NotEmpty validation을 활용할 수 있다
        //엔티티에서 바로 @NotEmpty라고 줄수도 있지만 매우 간단한 앱일때만... 실무에서는 엔티티 값에 직접적인 조작을 하진 않는다.
        //html에서 오는 정보를 validation 하는게 보통
        if(result.hasErrors()){
            return "members/createMemberForm"; //이때 따로 model로 넘기지 않지만 html에 있는 ${memberForm}과 이름이 같아야 한다.(다르면 인식 x)
        }
        Member member = new Member();
        String name = memberForm.getName();
        member.setName(name);
        member.setAddress(new Address(memberForm.getCity(),memberForm.getStreet(),memberForm.getZipcode()));

        memberService.join(member);
        return "redirect:/";
    }
    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
